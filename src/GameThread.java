public class GameThread extends Thread {
    private GamePanel gamePanel;
    private boolean running = false;

    public GameThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean getRunning() {
        return running;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfFPS = 60.0;
        double duration = 1000000000 / amountOfFPS; 
        double delta = 0;

        while (running) { 
            long now = System.nanoTime();
            delta += (now - lastTime) / duration;
            lastTime = now;

            if (delta >= 1) {
                gamePanel.move();
                gamePanel.checkCollision();
                gamePanel.repaint(); 
                delta--;
            } 
        }
    }
}
