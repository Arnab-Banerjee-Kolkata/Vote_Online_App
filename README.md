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
4. Booth automatic logout on focus change or window close: So that laptop cannot be used to take logged in booth outside office [IN PROGRESS]<br>
5. Every database update or create operation checks for logged in booth: Prevents attackers from crashing database<br>
<br>
6. Only one chance to vote per voter: Prevents attackers to repetedly try to vote using an aadhaar number[IN PROGRESS]<br>
<br>
7. Image verification at booth to verify the appearance of the voter<br>
8. Cross OTP verification via registered mobile number/biometric scan of voter: Prevents imposters to vote in the voter's stead. Prevents the booth staff to approve fake people.<br>
9. Incorporated approval, voted status and NOTA vote with voter verification: Prevents attacker from faking an approval as this needs logged in booth and voter's registered mobile number/biometrics<br>
10. Only one active approval per booth: Prevents booth to approve another voter unless the previous voter in line has finished voting. Ensures that only the approval of the head of the line is removed after a vote is cast. [IN PROGRESS]<br>
<br>
11. sms request is sent from logged in booth: Prevents attackers from repeatedly sending sms to exhaust the sms limits.[IN PROGRESS]<br>
<br>
<br>
12. Only constituency name, phase election Id and boothId are stored in approval: Helps maintain anonimity [IN PROGRESS]<br>
13. Voter has to enter booth OTP to re validate his presence in logged in booth: Prevents attackers from getting hold of the voting panel as the show panel and booth OTP verification are incorporated in same file. [IN PROGRESS]<br>
14. Head of line is cleared after a certain time automatically: Prevents attackers from blocking a booth<br> 
<br>
15. Random encryption key for each vote is sent from server: Prevents decryption of every vote with a single encryption key<br> 
<br>
16. Number of people voted and number of votes are matched: To maintain integrity and then a NOTA vote is removed to store the original vote. Head of the line is also cleared.<br>
<br>
<br>
17. Randomly encrypted votes are stored in the database: Prevents the ECI staff from viewing the exact votes.<br>
<br>
<br>
<br>
<br>
<b>PENDING THREATS:</b><br>
<br>
1. DDOS attack<br>
<br>
2. Booth staff opens website in his phone and logs in to booth(in office) and then without switching off his screen, takes the phone with him to create random elections to crash the database. [Probably solvable by using mac and ip address]<br>
