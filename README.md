# MULTI_KEY_AGREEMENT_PROTOCOL
Στο συμμετρικό κρυπτοσύστημα, δύο χρήστες διαθέτουν ένα μυστικό κλειδί και με αυτό έχουν τη δυνατότητα να εγκαθιδρύσουν ένα ασφαλές κανάλι επικοινωνίας. Το ερώτημα που προκύπτει με φυσικό τρόπο είναι: πώς αυτοί οι δυο χρήστες, που πιθανώς δεν έχουν καμία πρότερη μεταξύ τους σχέση, μπορούν να συμφωνήσουν σε ένα μυστικό κλειδί; Η κρυπτογραφία δημόσιου κλειδιού ή ασύμμετρη κρυπτογραφία λύνει αυτό το πρόβλημα διαμοιρασμού κλειδιού. Έτσι, δυο οντότητες μπορούν να συμφωνήσουν στη χρήση ενός μυστικού κλειδιού ακόμα κι αν ένας επιτιθέμενος παρακολουθεί το μεταξύ τους διάλογο σε ένα δημόσιο κανάλι, χωρίς να απαιτείται η ύπαρξη κάποιας ενδιάμεσης έμπιστης τρίτης οντότητας. Τα πρωτόκολλα που υλοποιούν αυτή τη λειτουργία ονομάζονται πρωτόκολλα συμφωνίας κλειδιού (key agreement protocols). Τα πρωτόκολλα συμφωνίας κλειδιού πρέπει να είναι σε θέση να εξασφαλίζουν: α) ορθότητα, δηλαδή ότι τα δύο μέρη έχουν καταλήξει στο ίδιο μυστικό κλειδί και β) ότι το κλειδί αυτό δεν μπορεί να διακριθεί από μια τιμή που προκύπτει από μια τυχαία ομοιόμορφη κατανομή. Στην πράξη αυτό σημαίνει ότι ένας επιτιθέμενος όχι μόνο δεν μπορεί να υπολογίσει τη μυστική τιμή αλλά δεν μπορεί καν να προβλέψει ούτε ένα τμήμα της. Στη συνέχεια παρουσιάζονται τρεις τρόποι μέσω των οποίων μπορεί να επιτευχθεί συμφωνία κλειδιού. Ενθυλάκωση: Το ένα μέρος (initiator) δημιουργεί ένα συμμετρικό κλειδί και το ενθυλακώνει (κρυπτογραφεί) με το δημόσιο κλειδί του άλλου μέρους (peer). Ακολούθως, αποστέλλει το ενθυλακωμένο κλειδί στον peer. Ο τελευταίος απενθυλακώνει το συμμετρικό κλειδί χρησιμοποιώντας το ιδιωτικό του κλειδί. Πρωτόκολλο Diffie-Hellman (DH): Δύο χρήστες καταλήγουν σε ένα κοινό μυστικό κλειδί ανταλλάσσοντας εξ’ αρχής μηνύματα σε ένα μη ασφαλές κανάλι. Το DH προσφέρει τη δυνατότητα εγκαθίδρυσης ενός μυστικού κλειδιού ανάμεσα σε δύο χρήστες. Πρωτόκολλο Station-to-Station(StS): Βασίζεται στο DH αλλά προσφέρει αμοιβαία αυθεντικοποίηση με τη χρήση πιστοποιητικών.