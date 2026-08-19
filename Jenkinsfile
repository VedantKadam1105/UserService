pipeline {
    agent any   // single-box setup: controller and agent are the same EC2

    environment {
        JAVA_HOME       = '/usr/lib/jvm/java-17-amazon-corretto.x86_64'
        PATH            = "${JAVA_HOME}/bin:${env.PATH}"
        AWS_REGION      = 'us-east-1'
        AWS_ACCOUNT_ID  = '253490772981'
        ECR_REPOSITORY  = 'user-service'
        IMAGE_NAME      = 'user-service'
        NAMESPACE       = 'hotel-rating-application'
        DEPLOYMENT_NAME = 'user-service'
        CONTAINER_NAME  = 'user-service'
        // No AWS credentials needed here -- the EC2's IAM role (Jenkins-Agent-Role) handles auth automatically
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    java -version
                    pwd
                    echo "Testing connectivity to $DATABASE_HOST:$DATABASE_PORT"
                    timeout 5 bash -c "cat < /dev/null > /dev/tcp/$DATABASE_HOST/$DATABASE_PORT" && echo "REACHABLE" || echo "NOT REACHABLE"
                    mvn clean install -e
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} -f Dockerfile ."
            }
        }

        stage('Push to ECR') {
            steps {
                sh '''
                    aws ecr get-login-password --region $AWS_REGION | \
                        docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                    docker tag $IMAGE_NAME:$BUILD_NUMBER \
                        $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-rating/$ECR_REPOSITORY:$BUILD_NUMBER

                    docker push \
                        $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-rating/$ECR_REPOSITORY:$BUILD_NUMBER
                '''
            }
        }

        stage('Deploy to EKS') {
            steps {
                sh '''
                    echo "Starting deployment to EKS..."
                    aws sts get-caller-identity

                    aws eks update-kubeconfig --region $AWS_REGION --name neostore-cluster

                    IMAGE_URI="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/hotel-rating/$ECR_REPOSITORY:$BUILD_NUMBER"
                    echo "New Image: $IMAGE_URI"

                    kubectl scale deployment/$DEPLOYMENT_NAME --replicas=1 -n $NAMESPACE

                    kubectl set image deployment/$DEPLOYMENT_NAME \
                        $CONTAINER_NAME=$IMAGE_URI -n $NAMESPACE

                    kubectl rollout status deployment/$DEPLOYMENT_NAME -n $NAMESPACE --timeout=300s

                    echo "Deployment completed successfully"
                    kubectl get pods -n $NAMESPACE
                '''
            }
        }
    }

    post {
        success { echo "Pipeline succeeded!" }
        failure { echo "Pipeline failed." }
    }
}