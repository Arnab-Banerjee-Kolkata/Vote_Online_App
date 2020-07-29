# Screen shots

<table width="500" border="0" cellpadding="5">

<tr>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/ph1.png" alt="description here" />
<br />
</td>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn2.png" alt="description here" height="285" width="150" />
<br />
</td>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn3.png" alt="description here" />
<br />
</td>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn4.png" alt="description here" />
<br />
</td>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn5.png" alt="description here" />
<br />
</td>

</tr>


<tr>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn6.png" alt="description here" />
<br />
</td>

<td align="center" valign="center">
<img src="https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/SampleImages/phn7.png" alt="description here" />
<br />
</td>

</tr>

</table>


# Demonstration Video

https://drive.google.com/file/d/1-8MAnlsEy63o3watSNZj3OkcaUjzvpmV/view?usp=sharing

# Vote_Online_App
<br>
<br>
<br>
<b>SECURITY MEASURES</b><br><br>

1. postAuthKey: Authentication key to validate that a request is made from our app or website<br>
2. smsAuthKey: Authentication key to validate that our app or website wants to use the paid sms service<br>
<br>
<br>
3. Booth OTP: To validate that no one can log in to booth outside the designated office<br>
4. Booth and admin automatic logout after 4 and 2 hours respectively: Prevents attacker from using booth for a long period of time<br>
<br>
5. Every database update or create operation checks for logged in booth: Prevents attackers from crashing database<br>
<br>
<br>
6. Only one chance to vote per voter: Prevents attackers to repetedly try to vote using an aadhaar number<br>
<br>
7. Image verification at booth to verify the appearance of the voter<br>
8. Cross OTP verification via registered mobile number/biometric scan of voter: Prevents imposters to vote in the voter's stead. Prevents the booth staff to approve fake people.<br>
9. Incorporated approval, voted status and NOTA vote with voter verification: Prevents attacker from faking an approval as this needs logged in booth and voter's registered mobile number/biometrics<br>
10. Only one active approval per booth: Prevents booth to approve another voter unless the previous voter in line has finished voting. Ensures that only the approval of the head of the line is removed after a vote is cast.<br>
11. Approval is expired after a few minutes: Prevents attacker from blocking booth line<br>
<br>
12. sms request is sent from logged in booth: Prevents attackers from repeatedly sending sms to exhaust the sms limits.<br>
<br>
<br>
13. Only constituency name, phase election Id and boothId are stored in approval: Helps maintain anonimity<br>
14. Voter has to enter booth OTP to re validate his presence in logged in booth: Prevents attackers from getting hold of the voting panel as the show panel and booth OTP verification are incorporated in same file.<br>
15. Head of line is cleared after a certain time automatically: Prevents attackers from blocking a booth<br> 
<br>
16. Random encryption key for each vote is sent from server: Prevents decryption of every vote with a single encryption key<br> 
<br>
17. Number of people voted and number of votes are matched: To maintain integrity and then a NOTA vote is removed to store the original vote. Head of the line is also cleared.<br>
<br>
<br>
18. Randomly encrypted votes are stored in the database: Prevents the ECI staff from viewing the exact votes.<br>
<br>
19. Customized random number generator: Prevents attacker from guessing the key number or OTP even with knowledge of pseudo random algorithm
<br>
20. Approval deletion check in current call before adding vote: Prevents attacker to sneak in a vote at the same time with the voter
<br>
21. Seperate database to store the vote bank: Prevents ECI staff to view the encrypted votes
<br>
22. Votes are stored in cluster: Prevents attacker to find out the order of votes
<br>
23. Server ip check: Prevents attacker from using postman/curl to make fake requests
<br>
24. Protection against SQL injection
<br>
25. Protection against Buffer overload
<br>
26. Otps in the database are encrypted: Database team member cannot login to a booth or admin account
<br>
27. Sms is only sent to valid voters who have not voted
<br>
28. At most 4 sms can be sent to a voter from a booth in an election: Prevents booth staff from depleting the sms limit
<br>
29. Alpha numeric OTP: Makes guessing nearly impossible
<br>
30. OTP to be entered in voting panel will be constant for a voter: Outsiders cannot change the OTP by making false attempts(When the approval is removed, the OTP will be changed)
<br>
31. Code encryption in app: Prevents attacker to get vital connection data by decompiling app
<br>
32. Total sms count for admin and booth: Helps to observe misuse of sms credits
<br>
33. Random token to be sent with panel which will be required to cast vote: Attacker cannot cast a vote from other device in the small time window allotted for voting

# App Link
https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App/blob/master/app/release/app-release.apk <br>

# Website Link
www.remote-voting.rf.gd <br>

# Related repos
Application: https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_App <br>
Website: https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_Website <br>
Public Election API: https://github.com/Arnab-Banerjee-Kolkata/Vote_Online_API <br>
Public Election API Usages: https://github.com/Arnab-Banerjee-Kolkata/Vote_API_Usages <br>
Private Poll API: https://github.com/Arnab-Banerjee-Kolkata/Private_Poll_API <br> 
Private Poll API Usages: https://github.com/Arnab-Banerjee-Kolkata/PrivatePoll_API_Usage <br>

# Developers
Arnab Banerjee: https://github.com/Arnab-Banerjee-Kolkata <br>
Abhijit Dey: https://github.com/bug-exterminator <br>
Adarsha Shaw: https://github.com/adarshashaw <br>
Rupsa De: https://github.com/RupsaDe <br>


