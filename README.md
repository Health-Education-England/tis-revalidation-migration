# TIS Revalidation Migration

## About
Revalidation migration service is the historical data migration ETL from the legacy Reval 
to the new Reval system.  

## TODO
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
This project is license under [The MIT License (MIT)](LICENSE).
