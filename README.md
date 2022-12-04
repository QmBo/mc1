# Main message service

## What is he doing?

When you start the loop, the service sends a message to the MC2 service via a websocket and waits until it receives 
a response via a POST request with the body containing the response message.
The MC2 service, in turn, forwards the message to the Kafka message broker for the MC3 service.
The MC3 service, in turn, forwards the message over the HTTP POST to the MC1 service.
Each service fills in the field with the current time for its service in the message before sending.
When MC1 receives a message via HTTP POST it adds an end cycle time and puts the message in the database. If the time 
set for the cycle execution has not ended, then the cycle repeats.

## Message example

```json
{
  "id": 1,
  "session_id": 12,
  "mc1Timestamp": 1670149253000,
  "mc2Timestamp": 1670149254000,
  "mc3Timestamp": 1670149255000,
  "endTimestamp": 1670149256000
}
```

`session_id` indicates the interaction number in the loop.

## Additional Information

### Service start at port `80` and have endpoints:
1. `/start` to start cycle message sending
2. `/stop` to terminate message sending

## How to run it

0. See peculiarities
1. Download `docker-compose.yml`
2. Run `docker-compose up`

## Peculiarities

According to the conditions of the task, access to the container with the database is required to stop the main logic.
There are minimum two options for implementing this condition.

1. Run a container with a database separately from the main logic.
    * Download files from a folder `splitStartCompode`. Then run for each subfolder. Start from the folder `db`
2. Stop logic by invoking a command `docker-compose stop`.
    * Download root directory files. Then launch.

In the first case, to access the database, use `docker exec -it maria_db mysql -u root -ppassword -D cycle`
In the second case, to access the database, use 
```bash
docker start maria_db
docker exec -it maria_db mysql -u root -ppassword -D cycle
```
