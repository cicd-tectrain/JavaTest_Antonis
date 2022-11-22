//Basis Pipeline Schema anlegen

pipeline {
  agent any

    //create environment
    environment {
        INTEGRATION_BRANCH = 'intergration'
        PRODUCTION_BRANCH = 'master'
    }

  stages{
        stage('Building Feature') {
          when {
                  branch 'feature/*'
                  beforeAgent true
                }

          //Agent overwrite and run in a docker container
          agent {
            docker {
                image 'gradle:7.5.1-jdk17-focal'
            }
          }


          steps {
            echo 'Building feature'
            sh 'ls -la'
            sh 'gradle clean build -x test'
          }
        }

        stage('Testing Feature') {
          when {
                  branch 'feature/*'
                  beforeAgent true
                }

        //Agent overwrite and run in a docker container
          agent {
            docker {
                image 'gradle:7.5.1-jdk17-focal'
            }
          }


          steps {
            echo 'Testing feature'
            sh 'gradle test'
          }

          //post-build
          post {
            always {
                //collect JUnit test  results
                junit 'build/test-results/**/*.xml'
            }
            success {
                        publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
            }
          }

        }

        stage('Integrating Feature') {
          when {
            branch 'feature/*'
            beforeAgent true
          }

          steps {
                  echo 'Integrating feature'
                  sh 'ls -la'
                  sh 'git branch -a'
                  sh 'git checkout ${BRANCH_NAME}'
                  sh 'git checkout ${INTEGRATION_BRANCH}'
                  sh 'git merge ${BRANCH_NAME}'
                  withCredentials ([
                        gitUsernamePassword(credentialsId: 'github_cicd_pat', gitToolName: 'Default')
                  ]) {
                        sh 'git push origin ${INTEGRATION_BRANCH}'
                  }
          }
        }

        // ====== Integration Stages ======
        stage('Build integration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Building integration'
                sh 'ls -la'
                sh 'gradle clean build -x test'
            }
        }

        stage('Test intergration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Testing integration'
                sh 'gradle test'
            }
        }


        stage ('Publish artifacts') {

            when {
                branch 'intergration'
                beforeAgent true
            }

            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Publishing artifacts'
                sh 'ls -la'

                nexusArtifactUploader artifacts: [[
                    artifactId: 'at.tectrain.cicd',
                    classifier: '',
                    file: 'build/libs/demo-0.0.1-SNAPSHOT.jar',
                    type: 'jar'
                ]],
                credentialsId: 'nexus_credentials',
                groupId: '',
                nexusUrl: 'nexus:8081',
                nexusVersion: 'nexus3',
                protocol: 'http',
                repository: 'repository/maven-snapshots',
                version: '0.0.1-SNAPSHOT'
            }
        }


        stage('Deploy intergration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            steps {
                echo 'Deploying integration'
                sh 'ls -la'
                sh 'git branch -a'
                sh 'git checkout ${BRANCH_NAME}'
                sh 'git checkout ${PRODUCTION_BRANCH}'
                sh 'git merge ${BRANCH_NAME}'
                withCredentials ([
                    gitUsernamePassword(credentialsId: 'github_cicd_pat', gitToolName: 'Default')
                ]) {
                sh 'git push origin ${PRODUCTION_BRANCH}'
                    }
            }
        }
  }
}