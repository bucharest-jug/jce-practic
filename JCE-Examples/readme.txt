Running the examples might require 'installing unlimited strenght jurisdiction files' (google search - copy in jdk\jre\lib\security).

Also, the Bouncy Castle provider is installed programmatically, but could also be manually installed
by editing the jdk/jre/lib/security/java.security file to add a new provider  (security.provider.11=org.bouncycastle.jce.provider.BouncyCastleProvider)
The provider libraries can be added to jdk/jre/lib/ext (but maven should take care already of dependancies).
