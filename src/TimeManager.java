public class TimeManager {

    public static double start;
    public static double duration;
    public static double timeLeft = 300.0;

    public static double setTimeAllowed(ChessGame game){
        int captures = game.getAllCaptures(game.turn).size();

        double diff;

        if(Celeste.history.size() > 0){
            double average = 0;
            for(GameHistory g: Celeste.history){
                average += g.difficultyIndex();
            }
            average /= Celeste.history.size();
            diff = Celeste.history.get(Celeste.history.size()-1).difficultyIndex();
            if(Celeste.history.size() < 25){
                diff = 1 + 2*(((diff - average)/average));
            } else {
                diff = 1 + (((diff - average)/average))*2/3;
            }

        }
        else diff = 1;

        diff += (captures - 2) * 0.1;


        if(Celeste.history.size() <= 25 && Celeste.history.size() >= 5){
            diff += Math.abs(15 - Math.abs(10-Celeste.history.size())) * 0.2;
        }

        if(getTimeLeft() < 5){
            diff = 1;
        }

        duration = (getTimeLeft()/40) * diff;
        System.out.println("diff: "+ diff +"  captures: "+captures);
        System.out.println("Time: "+duration);
        return duration;
    }

    public static void start(){
        start = System.currentTimeMillis();
    }

    public static boolean finished(){
        return (System.currentTimeMillis() - start) / 1000 >= duration;
    }

    public static double elapsed(){
        return (System.currentTimeMillis() - start) / 1000;
    }

    public static double getTimeLeft(){
        return timeLeft - elapsed();
    }

    public static void stop(){
        timeLeft -= duration;
        start = -1;
        duration = 0;
    }

}
