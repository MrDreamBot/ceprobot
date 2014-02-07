Exploring the World of CEP: Controlling Robots using CEP - Part 1
By Andy Yuen
Feb 8, 2014.

Table of Contents
1 Introduction	1
2 Desired Robot Behaviour	2
3 Model and Design	3
4 Obstacle Avoidance Rules	5
5 Differences between Knowledge and KIE APIs	7
6 Testing the Rules using JUnit	9
7 Summary	13

1 Introduction

As you all know, CEP stands for Complex Event Processing. According to Wikipedia:

“Event processing is a method of tracking and analyzing (processing) streams of information (data) about things that happen (events), and deriving a conclusion from them.”

Drools Fusion is responsible for enabling CEP on the Drools rule engine. This project uses Drools Fusion. It is assumed that you already know how a rule engine like Drools works.

Having nothing to do in my spare time at home (REALLY?), I'd like to create an application small enough for demo purposes and yet mirrors the architecture of Enterprise CEP applications. In other words, I want to build a small CEP application whose structure and architecture can be reused in an Enterprise environment. Many CEP applications examples can be found on the Internet including fraud detection in a banking environment so I am not going to do yet another one of those usual topics. I am going to create a CEP application to control robots. It will be fun as well as educational, for me anyway, in my journey into the world of CEP.

The requirements for the robot is minimal. It must support remote commands for motor control and have 3 distance sensors detecting whether there are obstacles in the front middle, front left or front right direction and be able to send that information to the CEP application as events via an adapter (to be designed in Part 2). Alternatively, it can have a scanning distance sensor (ie, a distance sensor mounted on top of a servo that moves from side to side) to provide the same information (more will be reviewed in Part 3). The behaviour of the robot is quite simple. It moves in a random direction. When it detects an obstacle in a its path, it just steers around it if the obstacle is not to close. If the obstacle is close-by, it will turn in place to avoid the obstacle. If it gets stuck in a place ie, turn in place left to right and right to left for several times without a clear passage way, it will turn around and move away from the obstacle. You may say, it is so simple that even a $20 Arduino can do it, why use CEP. You are right but remember that this is just the start. This approach can control a swarm of robots instead of just one as long as they have unique names and that they all send events to the CEP application (more on this later). Also more complex behaviours can be added in the form of adding more rules. And most of all, we are exploring what makes up a CEP application.

I am going to document my journey into the CEP world in three parts:

Part 1 – Drools rules and JUnit tests (this installment)
Part 2 – CEP Application Architecture (applies to enterprise CEP applications as well)
Part 3 – Controlling a Real Robot using CEP (the fun part of the project that utilises everything that has been discussed in the earlier installments)

Please Read the complete document: CepRobot-Part1.pdf. 

