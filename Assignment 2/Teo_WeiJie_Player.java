class Teo_WeiJie_Player extends Player {
    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        // First round - be nice, always cooperate.
        if (n == 0) return 0;

        // If any agent defected in the previous round,
        // punish immediately by defecting.
        if (oppHistory1[n - 1] == 1 || oppHistory2[n - 1] == 1) return 1;

        // Calculate the trustworthiness of each agent based on defection rate.
        // Trustworthy --> rate < 5%
        // Untrustworthy --> rate >= 20%
        // Neutral --> in between
        // Defection has a heavier emphasis than cooperation.
        // However, punishment should use "measured force", so there's a neutral range set up.
        int defections1 = 0;
        int defections2 = 0;
        for (int action : oppHistory1) defections1 += action;
        for (int action : oppHistory2) defections2 += action;
        double defectRate1 = 1.0 * defections1 / n;
        double defectRate2 = 1.0 * defections2 / n;

        // If any agent is not trustworthy, defect.
        // Even if both cooperated in the previous round.
        if (defectRate1 >= 0.2 || defectRate2 >= 0.2) return 1;
		
        // If both agents are trustworthy, cooperate.
        if (defectRate1 < 0.05 && defectRate2 < 0.05) return 0;
		
        // If any agent is neutral, go back one more round and check for defection.
		
        // Second round, can't go back any further, so just cooperate.
        // Shouldn't reach this code though given the above conditions.
        if (n == 1) return 0;
		
        // If any of the neutral agents defected, punish by defecting.
        // Otherwise, cooperate.
        if (defectRate1 >= 0.05 && oppHistory1[n - 2] == 1) return 1;
        if (defectRate2 >= 0.05 && oppHistory2[n - 2] == 1) return 1;
        return 0;
    }
}
