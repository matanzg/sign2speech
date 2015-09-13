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
            result = Unirest.post("https://ussouthcentral.services.azureml.net/workspaces/3e3f9b7831664c92a318566e1e94a410/services/d277b5a7113f42828a21bb5142c7dfee/execute?api-version=2.0")
                    .header("Authorization", "Bearer 4wFjmENJVytAyx0avluOXy2sxn71MBsW9z2W5JMEg87xk7pXgVgj9YJ9Lv5GNoapVGfvUM4n5k5WzZie0YxurQ==")
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
        for (char curLetter = '1'; curLetter <= '5' && ((numOfFeatures + 2 + curLetter - '1') < values.length()); curLetter++) {
            probabilityMap.put(String.valueOf(curLetter), values.getDouble(numOfFeatures + 1 + curLetter - '1'));
        }
        double max =  probabilityMap.values().stream().mapToDouble(x -> x.doubleValue()).max().getAsDouble();
        probabilityMap.clear();
        probabilityMap.put(values.getString(values.length() - 1), max);

        return probabilityMap;
    }

}
