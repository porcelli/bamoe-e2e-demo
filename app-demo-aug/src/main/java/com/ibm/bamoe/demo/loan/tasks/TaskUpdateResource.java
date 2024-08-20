package com.ibm.bamoe.demo.loan.tasks;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.bamoe.demo.loan.Config;

@Path("/task-update")
public class TaskUpdateResource {

    private static final Logger LOGGER = Logger.getLogger(TaskUpdateResource.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(UserTaskUpdateDTO taskUpdate) {
        try {
            LOGGER.info("Received task update request");
            
            // Build the URL for the remote service
            String url = String.format(Config.REMOTE_URL + "/CreditApplication/%s/Task/%s?phase=complete&user=admin",
                    taskUpdate.getProcessInstanceId(), taskUpdate.getId());

            // Create the JSON payload
            Map<String, String> payload = new HashMap<>();
            payload.put("status", taskUpdate.getStatus());
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Create the HttpClient and request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status
            if (response.statusCode() == 200) {
                LOGGER.info("Task update successfully sent to remote service");
                return Response.ok().build();
            } else {
                System.out.println(response.body());
                LOGGER.error("Failed to update task on remote service: " + response.statusCode());
                return Response.status(Response.Status.BAD_GATEWAY).entity("Failed to update task on remote service").build();
            }

        } catch (Exception e) {
            LOGGER.error("Error processing task update", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to process task update").build();
        }
    }
}
