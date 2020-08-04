# find-service

Quarkus based backend service for the Find My Relative demo application.

Frontend Service can be found here -`https://github.com/Emergency-Response-Demo/findmyrelative-frontend` 

# Running

- Locally

    Modify the `INCIDENT_SERVICE_URL` and `MISSION_SERVICE_URL` in `application.properties` file.

    Run `./mvnw compile quarkus:dev`
  
- Deploy on OpenShift

  You can deploy using Dockerfile on OpenShift Webconsole or run the following command.
  
  ```
  $ OCP_USERNAME=user1
  $ ERDEMO_NS=$OCP_USERNAME-er-demo    # CHANGE ME (if needed)

  $ oc new-app --name find-service \
          --strategy docker https://github.com/Emergency-Response-Demo/find-service \
          -n $ERDEMO_NS \
          --env=INCIDENT_SERVICE_URL=http://$OCP_USERNAME-incident-service:8080 \
          --env=MISSION_SERVICE_URL=http://$OCP_USERNAME-mission-service:8080
  ```
  
  You will need to pass `INCIDENT_SERVICE_URL` and `MISSION_SERVICE_URL` as Environment variable.
