# What is this
This is a simple example of a javaAgent that can be used to prevent the use of simple stealers (easyforme, and basic direct to discord stealers). While it was originally made to help me debug and delete basic stealers I realized that it could be used to prevent data exfiltration from a victims devices so thats what i've changed it into. Exit code 66 is what ive decided to use in the case of a discord or easyfor.me connection being established (due to error code 66 being "Payload execution failed").

# How do I use it
While its not useable in some minecraft clients (feather, lunar) most other clients allow you to insert custom `Java Arguments` in which case all you have to do is add this to the arguments `-javaagent:"path/to/the/agent"`. Currently this agent can only be used for 1.20.4+ versions of minecraft or any compiled file for java 21. 

# Whats to come
While I wanted to hook into whenever the minecraft access token is grabbed I wasnt able to due to minecrafts obfuscated mappings, but if I can find a work around that would be something im intrested in.
