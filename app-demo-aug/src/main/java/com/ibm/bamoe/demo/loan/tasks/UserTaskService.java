package com.ibm.bamoe.demo.loan.tasks;

import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClientBuilder;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.bamoe.demo.loan.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Path("/tasks")
public class UserTaskService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final DynamicGraphQLClient client;

    @Inject
    public UserTaskService() {
        this.client = DynamicGraphQLClientBuilder.newBuilder()
                .url(Config.REMOTE_URL + "/graphql")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserTaskInstanceDTO> getReadyUserTaskInstances() {
        Response response;

        String query = """
            {
              UserTaskInstances(where: {state: {equal: "Ready"}}) {
                processInstanceId
                id
                state
                inputs
              }
            }
            """;

        try {
            response = client.executeSync(query);
            return extractTaskInstanceDetails(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<UserTaskInstanceDTO> extractTaskInstanceDetails(JsonObject jsonResponse) throws Exception {
        List<UserTaskInstanceDTO> taskInstanceDTOList = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(jsonResponse.toString());
        JsonNode userTaskInstances = rootNode.get("UserTaskInstances");

        if (userTaskInstances.isArray()) {
            for (JsonNode instance : userTaskInstances) {
                String processInstanceId = instance.get("processInstanceId").asText();
                String id = instance.get("id").asText();
                String inputs = instance.get("inputs").asText();

                // Parse the inputs field
                JsonNode inputsNode = objectMapper.readTree(inputs);
                String name = inputsNode.get("name").asText();
                int score = inputsNode.get("score").asInt();
                double income = inputsNode.get("income").asDouble();

                // Create a new DTO and add it to the list
                UserTaskInstanceDTO dto = new UserTaskInstanceDTO();
                dto.setProcessInstanceId(processInstanceId);
                dto.setId(id);
                dto.setName(name);
                dto.setScore(score);
                dto.setIncome(income);

                taskInstanceDTOList.add(dto);
            }
        }

        return taskInstanceDTOList;
    }

}
