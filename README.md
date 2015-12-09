![Figure 1: The stages and the timing of the experiment.](https://raw.github.com/jozefdobos/gnret/master/docs/images/fig1.png)
<b>Figure 1:</b> The stages and the timing of the experiment.

# 1 Introduction
Currently at version 0.7.4, the Gender-Neutral Real-Effort Task (GNRET) is a collection of individual feedback driven end-user tasks and a master control application to monitor the ongoing experiment in real-time from a remote location. Under the supervision of [Dr. Victoria Prowse](https://faculty.cit.cornell.edu/vlp33/), this study was designed by Zdenka Kissová as a partial fullfilment of the requirements for the degree of M.Phil. in [Economics](http://www.economics.ox.ac.uk/) at [University of Oxford](http://www.ox.ac.uk/). The experiments were conducted in the experimental laboratory of the [Nuffield Centre for Experimental Social Sciences (CESS)](https://cess-nuffield.nuff.ox.ac.uk/) at [University of Oxford](http://www.ox.ac.uk/). The actual software was delivered by [Jozef Doboš](https://github.com/jozefdobos) from the [Department of Computer Science](http://www.cs.ucl.ac.uk/), [University College London](http://www.ucl.ac.uk/).

# 2 Program Flow
Figure 1 outlines the stages and timing of the experiment. Each experimental session lasts for approximately 90 minutes and consists of seven (one practice and six paying) 6-minute rounds. In each 6-minute round, the initial 3 minutes are allocated to the verbal component of the task (word spotting), followed by another 3 minutes of numerical questions (adding up) all of which are automatically timed out. There is a 4-minute break after each round. During the first minute of this break, participants can always see their individual total points score (the sum of the verbal and numerical point scores) for the respective round on their screens. In the last three minutes of the break, Treatment Group subjects receive treatment-specific relative performance feedback. The demographics and behavioural data questionnaire is displayed after the last experimental round. The questionnaire asks about basic demographics such as gender, age, country of origin and degree of study as well as the subject's perceived level of competitiveness, familiarity with the task, satisfaction with the performance and the potential use of performance goals.


<img src="https://raw.github.com/jozefdobos/gnret/master/docs/images/fig2a.png" alt="Figure 2: (a) The letter grid for word spotting used in the Practice Round across all sessions." width=430>&nbsp;<img src="https://raw.github.com/jozefdobos/gnret/master/docs/images/fig2b.png" alt="Figure 2: (b) The numerical grid with an adding up question used in the Practice Round across all sessions." width=430>

<b>Figure 2:</b> (a) The letter grid for word spotting used in the Practice Round across all sessions. (b) The numerical grid with an adding up question used in the Practice Round across all sessions.

## 2.1 Word Spotting Task
During the word spotting, each subject is presented with a 15 by 15 grid of capital letters, see Figure 2(a), and is asked to look for English words in horizontal, vertical or diagonal direction, forward or backward. A word is selected using the mouse (This ensures that the subjects are not disadvantaged based on their typing skills.). The subjects need to place the cursor of the mouse over the starting letter of a proposed word, pull the cursor to the last letter of that word and then release the cursor (The first and last letter recognition prevents pure guessing.). If the selected word is valid, it remains highlighted and a point is awarded. No points are awarded or subtracted for incorrect entries so as to avoid potential hostile framing or punishment effects in the subjects' behaviour.

## 2.2 Numerical Task
After the initial three minutes elapse, an adding up question for a pair of 2-digit numbers automatically appear on the screen. The subject needs to correctly sum the numbers to win a point and proceed to the next numerical question. Calculators, pens and paper are not allowed. The answer is entered using the mouse and a calculator-style number pad on the screen. As in the verbal component of the task, no points are awarded or subtracted for incorrect entries. The points from the verbal and numerical task components in a specic round are added up to form the total point score for that round. Identical letter grids and number questions in the same order are used across all sessions and hence all treatment groups.


<img src="https://raw.github.com/jozefdobos/gnret/master/docs/images/fig3a.png" alt="Figure 3: Individual and relative performance feedback as displayed during (a) the first and (b) the last three minutes of the break to Treatment Group 1. Note that the first-minute individual performance feedback was displayed in the same manner to all subjects across the experiment." width=430>&nbsp;<img src="https://raw.github.com/jozefdobos/gnret/master/docs/images/fig3b.png" alt="Figure 3: Individual and relative performance feedback as displayed during (a) the first and (b) the last three minutes of the break to Treatment Group 1. Note that the first-minute individual performance feedback was displayed in the same manner to all subjects across the experiment." width=430>

<b>Figure 3:</b> Individual and relative performance feedback as displayed during (a) the first and (b) the last three minutes of the break to Treatment Group 1. Note that the first-minute individual performance feedback was displayed in the same manner to all subjects across the experiment.

## 2.3 Performance Feedback
Subjects are divided into four groups with the same selection and order of the tasks, but with different performance feedback as follows:
- Control Group (C) - no relative performance feedback.
- Treatment Group 1 (T1) - computerised private relative performance feedback.
- Treatment Group 2 (T2) - personal private relative performance feedback.
- Treatment Group 3 (T3) - personal public relative performance feedback.

<img src="https://raw.github.com/jozefdobos/gnret/master/docs/images/fig4.png" alt="Figure 4: FTP Login window showing location of the log file." width=250>

<b>Figure 4:</b> FTP Login window showing location of the log file.

# 3 Technical Information
The front-end Graphical User Interface (GUI) is written in Java Swing with networking provided by the Apache Commons library executed in an exclusive full-screen mode to avoid any unnecessary distractions.

Once started, each client application, the one that the subjects interact with, opens up a File Transfer Protocol (FTP) connection to the master server from which it polls for a start and every round signals triggered by the admin interface. Only if the corresponding buttons are set, see Figure 5 for toolbar options, will the individual sections as outlined in Figure 1 automatically proceed. Client saves locally a text le with all so far collected responses which is uploaded to the main FTP server during each break. Admin program automatically calculates the rank of each client and saves the results to the FTP server from which the client if in T1 mode requests this information and displays it on the screen. Intermediate results as well as the final questionnaire answers are viewable in the admin interface.

## 3.1 Error Handling
In the case of a crash, there is always a local copy of the subject's responses as well as a program log file. If the FTP connection is dropped, the client will automatically attempt reconnection with a randomised interval of 30 to 60 seconds in between as to prevent overloading the server.

## 3.2 Requirements
- Java Runtime Environment v1.4+.
- Java Webstart plugin.
- MS Windows or Linux based FTP server with password protected write access. 
 
Downloadable from http://www.java.com/en/download/index.jsp

![Figure 5: FTP Login window showing location of the log file.](https://raw.github.com/jozefdobos/gnret/master/docs/images/fig5.png)

<b>Figure 5:</b> Admin interface for live monitoring of the experiment.

## 3.3 External Dependencies
<dl><dt>Apache Commons Net 2.2.</dt><dd>This standard Apache release library provides networking functionality through FTP and socket connection wrappers and is distributed under the Apache License Version 2.0 (January 2004), can be found at: http://www.apache.org/
licenses/.</dd>
<dt>Java File Graphics 1.0.</dt><dd> Former Sun Microsystems created a standard icon graphics library
to distribute with their Java Swing applications.</dd> 
<dt>Jazzy Core Spell Checker.<dt><dd>Jazzy Core implements spell checking algorithm similar to as
pell and is distributed under GNU Lesser General Public License Version 2.1 (February 1999). The source and precompiled libraries can be found at: http://sourceforge.net/projects/jazzy. </dd>
<dt>ISpell Word Lists.</dt><dd>A word list compiled from American and British English spelling lists of the Ispell Version 3.1.20 distributed under a royalty free license, a copyright 1993 of Geoff Kuenning, Granada Hills, CA, USA. The individual dictionaries can be found at: http://wordlist.sourceforge.net/.</dd>
</dl>
