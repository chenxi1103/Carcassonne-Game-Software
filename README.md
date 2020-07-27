### Homework 4c is turned in March 27th (Wednesday) - use 2 grace day. Thanks!<br><br>
Homework 4c information:
The runnable program is located in edu.cmu.cs.cs214.hw4.gui.GUI, if the game cannot be run by "gradle run", please run it manually @ edu.cmu.cs.cs214.hw4.gui.GUI main function. Thanks!


PS: As there are lots of flaws in 4a, I changed a lot based on my original design, so the actual class/methods may be differ from what are specified in 4a. I will update the new design document in 4c. Sorry for that!
# Carcassonne Game 4b test case illustration
#### For better demonstrate the test case, here are the illustration for each unit test case

## GameBoardTest: testPlaceTile_RoadMerge
I chose this test condition because under this condition, a road merge would happen.<br><br>
![placeTile&mergeRoad](https://github.com/CMU-17-214/chenxili/blob/master/homework/4/src/main/resources/testcaseInfo/mergeRoad.png)

## GameBoardTest: testPlaceTile_CityMerge
I chose this test condition because under this condition, a city merge would happen.<br><br>
![placeTile&mergeCity](https://github.com/CMU-17-214/chenxili/blob/master/homework/4/src/main/resources/testcaseInfo/mergeCity.png)

## GameBoardTest: testPlaceTile_Monastery
I chose this test condition because under this condition, a monastery feature is complete and the score is calculated<br><br>
![placeTile&mergeCity](https://github.com/CMU-17-214/chenxili/blob/master/homework/4/src/main/resources/testcaseInfo/monasteryCompleteandScore.png)

## GameSystemTest: testCalculateScore
I chose this test condition because under this condition, both city and road features are completed and need to be correctly calculated. (Monastery score is tested in testPlaceTile_Monastery)
* Player1 : 8 scores for city complete
* Player0 : 7 scores for road complete
* Player4 : 7 scores for road complete (share with player0) <br><br>
![placeTile&mergeCity](https://github.com/CMU-17-214/chenxili/blob/master/homework/4/src/main/resources/testcaseInfo/normal_score.png)

## GameSystemTest: testFinalizeScore
I chose this test condition because the "rules" use this test condition to illustrate how to finalize the score when we run out of tiles and end the game.
* Player0 : 3
* Player1 : 3 
* Player2 : 8 
* Player3 : 4 
* Player4 : 0 <br><br>
![placeTile&mergeCity](https://github.com/CMU-17-214/chenxili/blob/master/homework/4/src/main/resources/testcaseInfo/finalizescore.png)
