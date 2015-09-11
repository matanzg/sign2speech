package org.tomblobal.sf.ml;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.util.*;

public class AzureProbabilityClient {

    private Gson gson;

    public AzureProbabilityClient() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public Optional<SortedMap<Character, Double>> getProbability(long[] features) throws UnirestException {

        List<Long> nullableFeaturesList = new ArrayList<>(features.length + 1);
        nullableFeaturesList.add(null);
        nullableFeaturesList.addAll(Longs.asList(features));

        String featuresJson = gson.toJson(nullableFeaturesList.toArray());
        // I should switch the Unirest thread pool with one of my own
        HttpResponse<JsonNode> result = Unirest.post("https://ussouthcentral.services.azureml.net/workspaces/f7470dd0449242fea6ac4f54b0aad1b8/services/a97e343bb34e47d4878f28f5b85c8897/execute?api-version=2.0")
                .header("Authorization", "Bearer t76gAAbh84JzBhIhWCJX/Ui3EaZOqzrUTTHC3OMbgc2mMR4Y9xHPTvS2YZzq/ChWcYlYzN9CtsPJZxQ5WCnuGw==")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{" +
                        "\"Inputs\": {" +
                        "  \"input1\": {" +
                        "   \"Values\": [" +
                        featuresJson +
                        "      ]" +
                        "    }" +
                        "  }" +
                        "}")
                .asJson();

        if (result.getStatus() != 200) {
                return Optional.absent();
        } else {
            Map<Character, Double> probabilityMap = getMapFromJson(result.getBody(), features.length);
            Ordering<Character> orderDescendingByValue = Ordering.natural().reverse().onResultOf(Functions.forMap(probabilityMap)).compound(Ordering.natural());
            return Optional.of(ImmutableSortedMap.copyOf(probabilityMap, orderDescendingByValue));
        }
    }

    private Map<Character, Double> getMapFromJson(JsonNode body, int numOfFeatures) {
        JSONArray values = body.getObject().getJSONObject("Results").getJSONObject("output1").getJSONObject("value")
                .getJSONArray("Values").getJSONArray(0);

        Map<Character, Double> probabilityMap = new HashMap<>(26);
        for (char curLetter = 'A'; curLetter <= 'Z'; curLetter++) {
            probabilityMap.put(curLetter, values.getDouble(numOfFeatures + 1 + curLetter - 'A'));
        }

        return probabilityMap;
    }

}
