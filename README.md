# Quiz Leaderboard System – Backend Integration Assignment

## Candidate Details
**Name:** Gunna Sree Yarramaneni  
**Reg No:** AP23110010375  

---

<<<<<<< HEAD
Final Score Submitted:
835
<img width="641" height="521" alt="image" src="https://github.com/user-attachments/assets/16307781-175b-43d5-9225-1e4390938fac" />
=======
## Objective

Build a Java-based backend system that:

- Polls validator API 10 times
- Handles duplicate API responses correctly
- Aggregates participant scores
- Generates leaderboard sorted by totalScore
- Computes total score
- Submits leaderboard once

---

## Approach

### Step 1: Poll API

API endpoint called 10 times:

GET /quiz/messages?regNo=AP23110010375&poll=0–9

Maintained mandatory **5-second delay** between requests.

---

### Step 2: Deduplication Logic

Duplicate responses handled using:

Ensures repeated events are ignored.

---

### Step 3: Score Aggregation

Used:

- HashMap → store participant scores
- HashSet → detect duplicates

---

### Step 4: Leaderboard Generation

Leaderboard sorted in descending order:

---

### Step 5: Final Submission

Submitted total score:

Validator response confirmed successful processing.

---

## Technologies Used

Java  
HttpClient API  
org.json library  

---

## How to Run

Compile:

---

## Result

✔ 10 API polls completed  
✔ Duplicate handling implemented  
✔ Leaderboard generated correctly  
✔ Total score submitted successfully  
>>>>>>> 0e33690 (Improved README documentation)
