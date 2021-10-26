# TIS Revalidation Migration

## About
Revalidation migration service is the historical data migration ETL from the legacy Reval 
to the new Reval system.

The migration ETL is written in Spring Batch in tis-revalidation-migration service. 

It consists of an ItemReader to read data from the legacy Reval MySQL database, an
ItemProcessor to map the trainee data into the format we need in the new Reval, and an
ItemWriter to insert the data into new Reval MongoDB.

## Run the migration
Prerequisites:

- The MySQL user `reval-migration_user` is used to access the source MySQL database.
    Both READ and WRITE permission are needed for the `revalidation` database, while only READ 
    permission is needed for the `auth` database.
- The MongoDB user specified in the properties is used to write to the target collection. 
    readWrite permission on `revalidation` database in MongoDB is needed.

1. Clear/drop the revalidation.recommendation collection in the Reval MongoDB
2. The migration will start automatically once the service runs.  To run the task manually:
    1. Find the subnet id for the AZ the database is in, e.g.

        ``export SUBNET_ID=`aws ec2 describe-subnets --output text --query "Subnets[*].SubnetId" 
        --filters Name=tag:Name,Values=tis_preprod_private_*` ``

    2. Find the security group to run with, e.g.

        ``export SECURITY_GROUP=`aws ec2 describe-security-groups --filters 
        Name=group-name,Values=tis_preprod* --output text --query "SecurityGroups[*].GroupId"` ``

    3. Now run the task with the specific task-definition version, e.g.

        `aws ecs run-task --task-definition tis-revalidation-migration:25 --cluster 
        revalidation-preprod --network-configuration
        "awsvpcConfiguration={subnets=[$SUBNET_ID],securityGroups=[$SECURITY_GROUP]}"`

3. Data will be populated to the revalidation.recommendation collection in the Reval MongoDB after migration.

## Exception Alerting
 - Sentry:
    - Set up Sentry project.
    - Provide `SENTRY_DSN` and `SENTRY_ENVIRONMENT` as environmental variables
   during deployment.

## Workflow
The `CI/CD Workflow` is triggered on push to any branch.

![CI/CD workflow](.github/workflows/ci-cd-workflow.svg "CI/CD Workflow")

## Versioning
This project uses [Semantic Versioning](semver.org).

## License
This project is licensed under [The MIT License (MIT)](LICENSE).
