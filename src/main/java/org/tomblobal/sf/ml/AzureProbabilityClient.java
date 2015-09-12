package org.tomblobal.sf.ml;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
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

    public Optional<SortedMap<String, Double>> getProbability(double[] features) {

        List<Double> nullableFeaturesList = new ArrayList<>(features.length + 1);
        nullableFeaturesList.add(null);
        nullableFeaturesList.addAll(Doubles.asList(features));

        String featuresJson = gson.toJson(nullableFeaturesList.toArray());
        // I should switch the Unirest thread pool with one of my own
        HttpResponse<JsonNode> result = null;
        try {
            result = Unirest.post("https://ussouthcentral.services.azureml.net/workspaces/f7470dd0449242fea6ac4f54b0aad1b8/services/64d601ff26bc4da89b5012376a783104/execute?api-version=2.0")
                    .header("Authorization", "Bearer u+e3rS1TerYAdIic0QBKtNb6ZPajqTLku9ENPCGkoH00eFm6WajRCkhdHFOvF9H9mL7a0YOP6H3M0vQr6mCgVg==")
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
        } catch (UnirestException e) {
            throw Throwables.propagate(e);
        }

        if (result.getStatus() != 200) {
                return Optional.absent();
        } else {
            Map<String, Double> probabilityMap = getMapFromJson(result.getBody(), features.length);
            Ordering<String> orderDescendingByValue = Ordering.natural().reverse().onResultOf(Functions.forMap(probabilityMap)).compound(Ordering.natural());
            return Optional.of(ImmutableSortedMap.copyOf(probabilityMap, orderDescendingByValue));
        }
    }

    private Map<String, Double> getMapFromJson(JsonNode body, int numOfFeatures) {
        JSONArray values = body.getObject().getJSONObject("Results").getJSONObject("output1").getJSONObject("value")
                .getJSONArray("Values").getJSONArray(0);

        Map<String, Double> probabilityMap = new HashMap<>(26);
        for (char curLetter = 'A'; curLetter <= 'Z'; curLetter++) {
            probabilityMap.put(String.valueOf(curLetter), values.getDouble(numOfFeatures + 1 + curLetter - 'A'));
        }

        return probabilityMap;
    }

}
