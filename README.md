# multi-microservices
A full-stack web application that allows users to manage tasks efficiently with features like task categorization, deadlines, priority settings, and notifications. Built with a focus on clean code, scalability, and responsive design.

Key Features:

User authentication with JWT and secure password encryption
CRUD operations for tasks with real-time updates
Responsive UI built with React and Material-UI
Backend REST API developed in Node.js with Express
MongoDB for database management with Mongoose ODM
Deployed using Docker and hosted on AWS

Technologies Used:
React, Node.js, Express.js, MongoDB, JWT, Docker, AWS

Achievements / Highlights:
Implemented task reminders via email notifications using Nodemailer
Optimized backend queries reducing response time by 40%
Practiced CI/CD with GitHub Actions for automated deployment
If you want, I can also create a more concise, one-line version suitable for a resume bullet point that’s punchy and impactful for hiring managers.



 echo "# click-analytics" >> README.md
 git init
 git add README.md
 git commit -m "first commit"
 git branch -M main
 git remote add origin
 https://github.com/sanyogitaholkar02/click-analytics.git
 git push -u origin main
 
To Create topic 
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ad-click-topic-1

To create Consumer or group
docker exec -it broker /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic ad-click-topic-1 --group ad-clicks-group-1 --from-beginning

docker exec -it broker /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
