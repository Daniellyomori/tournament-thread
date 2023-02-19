import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Tournament {

    private List<Integer> competitor;
    private final HashMap<Integer,Integer> score = new HashMap<>();

    public Tournament(int round, int nCompetitor) throws Exception {
        for(int i = 0; i <round; i++){
            startTournament(nCompetitor);
            updateScore(nCompetitor);
        }

        LinkedHashMap<Integer, Integer> sortedScore = score.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) ->oldValue, LinkedHashMap::new
                ));

        System.out.println("==== TOP 3 ====");
        for(int i = 1; i< 4; i++){
            System.out.println("Competitor #" + i + " with " + sortedScore.get(i) + " points");
        }

        System.out.println("");
        System.out.println("==== FINAL SCORE ====");
        for(Map.Entry<Integer, Integer> competitor : sortedScore.entrySet()){
            System.out.println("Competitor #" + competitor.getKey() + " with " + competitor.getValue() + " points");
        }
    }

    public void checkpoint(int competitor) {
        synchronized (this.competitor) {
            this.competitor.add(competitor);
         
        }
    }

    public void updateScore(int qtde) throws Exception{
        int count = qtde;
        for(int i = 0; i < qtde; i++){
            int indice = competitor.get(i);
            if(score.get(indice) == null){
                score.put(indice, count);
            }
            else{
                int sum = score.get(indice) + count;
                score.put(indice, sum);
            }
            count--;
        }

    }

    public void startTournament(int n) throws Exception{
        this.competitor = new ArrayList<>();

        final var threads = new ArrayList<Thread>();

        for (int i = 1; i < (n+1); i++) {
            final int current = i;
            threads.add(new Thread(() -> checkpoint(current)));
        }

        threads.forEach(Thread::start);
        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 20));

    }

    public static void main(String[] args) throws Exception {
        Scanner entrada= new Scanner(System.in);

        System.out.println("---------------WELCOME TO THE THREADS TOURNAMENT---------------");

        System.out.println("How many rounds will be? ");
        int nRounds = entrada.nextInt();

        System.out.println("How many competitors will participate? ");
        int nCompetitors = entrada.nextInt();

        new Tournament(nRounds, nCompetitors);
    }
}