#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${rootArtifactId} game example is a free and open source game.

The game can be run by double clicking on the ${rootArtifactId}-game-${symbol_dollar}{project.version}.jar
file or using one of the run-* script.

It will run in fullscreen
by default, but this can be configured with the options menu or in the 
~/.config/${rootArtifactId}/config.properties file ( '~' is your home directory).

THE GAME WILL ONLY WORK IF:
 - YOU HAVE INSTALLED JAVA 1.7 OR HIGHER. (HEADLESS JAVA VERSION WONT WORK).
 - YOU HAVE AN OPENGL ENABLED COMPUTER. THAT MEANS A DECENT GRAPHIC CARD WITH
   DECENT DRIVER.
 - A SUPPORTED RESOLUTION BY YOUR GRAPHIC CARD AND YOUR SCREEN IS CHOSEN IN 
   THE ~/.config/${rootArtifactId}/config.properties FILE.

If it does not work, you can try to get debug messages by running the game
like this:

java -jar -Dorg.lwjgl.util.Debug=true ${rootArtifactId}-game-${symbol_dollar}{project.version}.jar

Go to http://devnewton.bci.im for news and updates.