# Overview

Totoro is a simple project I'm using to explore some APIs and clean patterns that I may apply at work.



# Required Environment Parameters

* -Dironmq.projectid= (get from Trever or use your own)
* -Dironmq.token= (ditto)
* -Dspring.profiles.active=seed (not required unless you want data seeded into your schema upon startup)

#Setting up Server.xml
You will need several JNDI settings to get the server working.  By default the server will attempt to run with Oracle,
but you can override parameters via JNDI.

Check the GlobalNamingResources.xml document.
