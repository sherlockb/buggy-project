# buggy-project
Hands-on exam for Java Developer applicants

## Requirements
- MySQL
- Java 17

# Multitenant Projects Overview

## multitenant-manager
This project enables managing connections to multiple databases based on tenants. It uses a routing data source that directs database requests based on the current tenant stored in the `currentTenant` variable. You can either define tenant configurations beforehand or use a custom `tenantResolver` function to dynamically resolve them.

## multitenant-model
This project defines the entities.

## multitenant-parent
This project contains the definition of dependencies that is common across the project.

## multitenant-service
This project contains controllers which define application API endpoints and handle HTTP requests.

## Environment Variables
To run the application, ensure the following environment variables are set:

- `RDS_DB_NAME`
- `RDS_USERNAME`
- `RDS_PASSWORD`
- `RDS_HOSTNAME`
- `RDS_PORT`

### Example using JVM arguments:
```sh
-DRDS_DB_NAME="central" -DRDS_USERNAME="root" -DRDS_PASSWORD="Passw0rd@1" -DRDS_HOSTNAME="localhost" -DRDS_PORT="3306"
```
## SQL Dump Restore
The SQL dump required for this project should be restored from the following location:

[SQL Dump](files/sql-dumps)

## Sequence Diagram
![Sequence Diagram](https://github.com/sherlockb/buggy-project/blob/main/files/Screen%20Shot%202024-08-06%20at%203.36.11%20PM.png)
