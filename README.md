# buggy-project
for Java Developer aapplicants

# Multitenant Projects Overview

## multitenant-manager
This project enables managing connections to multiple databases based on tenants. It uses a routing data source that directs database requests based on the current tenant stored in the `currentTenant` variable. You can either define tenant configurations beforehand or use a custom `tenantResolver` function to dynamically resolve them.

## multitenant-model
This project defines the entities.

## multitenant-parent
This project contains the definition of dependencies that is common across the project.

## multitenant-service
This project contains controllers which define application API endpoints and handle HTTP requests.

## Sequence Diagram
![Sequence Diagram](https://github.com/sherlockb/buggy-project/blob/main/files/Screen%20Shot%202024-08-06%20at%203.36.11%20PM.png)
