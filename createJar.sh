#!/bin/bash

javac *.java
jar cvfm GospelTopicsFinder.jar Manifest.txt Finder.class Topic.class
