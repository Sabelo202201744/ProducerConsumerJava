Producer–Consumer Problem - Java Implementation
Student Name: Matsebula Sabelo J
Student Number: 202201744
Program: B.Sc.IT

Student Name: Mkhontfo Lungani B
Student Number: 202202393
Program: B.Sc.IT

Project Overview
This project is an implementation of the Producer–Consumer Problem using Java.
It demonstrates:
1.	XML wrapping and unwrapping of IT Student data
2.	Multithreaded Producer–Consumer using semaphores and a bounded buffer
3.	Socket-based Producer–Consumer over TCP
4.	A video demonstration explaining how all components work

Features
Part 1: XML Data Wrapping / Unwrapping
•	ITStudent objects automatically generated with:
    	Name
    	8-digit student number
    	Programme
    	List of courses and marks

•	XML representation using DOM
•	XML parsing back into Java objects

Part 2: Multithreaded Producer-Consumer
•	Producer and Consumer implemented as threads
•	Uses:
    	Semaphore empty → controls empty spaces
    	Semaphore full → controls filled spaces
    	Semaphore mutex → protects shared buffer

•	Buffer size = 10
•	Producer:
    	Generates XML files (student1.xml … student10.xml)
    	Inserts a number (1–10) into buffer

•	Consumer:
    	Reads file number from buffer
    	Reads and parses XML
    	Prints student info, average, pass/fail
    	Deletes file after consumption

Part 3: Socket-Based Producer-Consumer
•	Two independent Java programs:
    	ProducerSocketServer
    	ConsumerSocketClient

•	Producer acts as a server, sending XML data over TCP
•	Consumer acts as a client, receiving XML and processing it
•	Demonstrates distributed computing version of Producer–Consumer

Part 4: Video Demonstration
Includes:
•	Explanation of all parts
•	Live demonstration of:
    	Thread-based producer/consumer
    	Socket-based communication

•	Overview of buffer synchronization and XML processing

How to Run the Project
Prerequisites
•	Java JDK 17+
•	VS Code with Java Extension Pack (or IntelliJ / Eclipse)

Running the Multithreaded Version
From the project root:
Compile:
javac -d bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

Run:
java -cp bin Main

Press ENTER to stop the program.

Running the Socket-Based Version
Open two terminals.

Terminal 1 - Start Server
java -cp bin socket.ProducerSocketServer

Terminal 2 - Start Client
java -cp bin socket.ConsumerSocketClient

Press CTRL + C to stop each one.

Project Structure
ProducerConsumerJava/
├── src/
│   ├── Main.java
│   ├── concurrency/
│   │   ├── BoundedBuffer.java
│   │   ├── Producer.java
│   │   └── Consumer.java
│   ├── socket/
│   │   ├── ProducerSocketServer.java
│   │   └── ConsumerSocketClient.java
│   ├── model/
│   │   └── ITStudent.java
│   └── util/
│       └── XMLUtil.java
├── bin/
├── shared/  (for Part 2 file exchange)
└── README.md

Technologies Used
•	Java 17
•	DOM XML Parser
•	Java Sockets
•	Java Concurrency (Threads + Semaphores)
•	VS Code
