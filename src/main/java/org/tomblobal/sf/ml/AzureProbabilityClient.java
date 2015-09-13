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
import com.sun.org.apache.xml.internal.utils.StringComparable;
import org.json.JSONArray;

import java.util.*;
import java.util.stream.Collectors;

public class AzureProbabilityClient {

    private final List<String> classes;
    private Gson gson;

    public AzureProbabilityClient(List<String> classes) {
        this.classes = classes.stream().sorted().collect(Collectors.toList());
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
            result = Unirest.post("https://ussouthcentral.services.azureml.net/workspaces/3e3f9b7831664c92a318566e1e94a410/services/5209322400ea4745a70b3b59bd8a3b24/execute?api-version=2.0")
                    .header("Authorization", "Bearer dLK8hAQMhg/YGQTAH1WJSq8kp4up3gx+Qq2Ml8jvxrDW3ZUvyBLes2htIKL8fgzKj3wkpsWleMCVCP4ugx/bTQ==")
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

        Map<String, Double> probabilityMap = new HashMap<>(classes.size());
        for (int classIndex = 0; classIndex < classes.size(); classIndex++) {
            probabilityMap.put(classes.get(classIndex), values.getDouble(numOfFeatures + classIndex));
        }

        return probabilityMap;
    }

}
