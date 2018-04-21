package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Cashable;
import net.mostlyoriginal.game.component.ChainColor;
import net.mostlyoriginal.game.component.Chainable;
import net.mostlyoriginal.game.system.GridSnapSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class ChainingSystem extends FluidIteratingSystem {

    private static final int WIDTH = 20; // base + 1 for easier scanning
    private static final int HEIGHT = 12; // base + 1 for easier scanning/
    private static final int MAX_CHAINS = WIDTH * HEIGHT; // too many but safe. :)
    private static final int MAX_PITSTOP_CHAINS = 20;
    private int MAX_CHAIN_LENGTH = 10;

    private GridSnapSystem gridSnapSystem;
    private final Cell[][] grid = new Cell[HEIGHT][WIDTH];
    private final Chain[] chains = new Chain[MAX_CHAINS];
    private final Chain[] pitstopChains = new Chain[MAX_PITSTOP_CHAINS];
    private int activeChains = 0;
    private int activePitstopChains = 0;
    private int cameraGridOffset;

    class Chain {
        public int length;
        public boolean broken;
        public final Cell[] cells = new Cell[MAX_CHAIN_LENGTH];

        void reset() {
            length = 0;
            broken = false;
        }

        public void add(Cell cell) {
            cells[length++] = cell;
        }
    }

    class Cell {
        E eCar;
        E ePitstop;
        Chain chain;
        Chain pitstopChain;
        int x;
        int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void reset() {
            eCar = null;
            ePitstop = null;
            chain = null;
            pitstopChain = null;
        }
    }

    public ChainingSystem() {
        super(Aspect.all(Chainable.class, Pos.class));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }
        for (int c = 0; c < MAX_CHAINS; c++) {
            chains[c] = new Chain();
        }
        for (int c = 0; c < MAX_PITSTOP_CHAINS; c++) {
            pitstopChains[c] = new Chain();
        }
    }

    @Override
    protected void begin() {
        super.begin();
        resetGrid();
        cameraGridOffset = GridSnapSystem.gridX(entityWithTag("camera")) - 3;
    }

    private void resetChains() {
        for (int c = 0; c < activeChains; c++) {
            chains[c].reset();
        }
        activeChains = 0;
        for (int c = 0; c < activePitstopChains; c++) {
            pitstopChains[c].reset();
        }
        activePitstopChains = 0;
    }

    private void resetGrid() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x].reset();
            }
        }
    }

    protected void end() {
        super.end();

        collectChains();

        rewardLongColorChains();
        rewardUnbrokenPitstopChains();

        resetChains();
    }

    private void rewardUnbrokenPitstopChains() {
        for (int c = 0; c < activePitstopChains; c++) {
            final Chain chain = pitstopChains[c];
            if (!chain.broken) {
                cashoutChain(chain, Cashable.Type.PITSTOP);
            }
        }
    }

    private void cashoutChain(Chain chain, Cashable.Type type) {
        for (int i = 0; i < chain.length; i++) {
            prepareForReward(chain.cells[i].eCar
                    .cashableChainLength(chain.length)
                    .cashableType(type), chain.cells[i]);
        }
    }

    private void rewardLongColorChains() {
        for (int c = 0; c < activeChains; c++) {
            final Chain chain = chains[c];
            if (chain.length >= 3) {
                cashoutChain(chain, Cashable.Type.COLOR);
            }
        }
    }

    private void prepareForReward(E e, Cell cell) {
        e
                .removeChainable();
    }

    private void collectChains() {
        // scan north-east, excluding outer rim to speed up visit logic.
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                final Cell cell = grid[y][x];

                if (cell.eCar != null && cell.chain == null) {
                    // new car chain!
                    visit(cell, chains[activeChains++], cell.eCar.chainableColor());
                }

                if (cell.ePitstop != null && cell.pitstopChain == null) {
                    // new pitstop chain!
                    visitPitstop(cell, pitstopChains[activePitstopChains++]);
                }
            }
        }
    }

    private void visitPitstop(Cell cell, Chain c) {
        // already part of a different chain, or no pitstop?
        if (cell.pitstopChain != null || cell.ePitstop == null) return;

        // We don't care about pitstops with (wrong) cars.
        if (cell.eCar == null || cell.eCar.chainableColor() != cell.ePitstop.chainableColor()) {
            c.broken = true;
        }

        cell.pitstopChain = c;
        c.add(cell);

        // these chains can only be orthogonal.
        for (int i = 0; i < 4; i++) {
            final int nx = cell.x + dx[i];
            final int ny = cell.y + dy[i];
            if (nx >= 0 && ny >= 0 && nx < WIDTH && ny < HEIGHT) {
                visitPitstop(grid[ny][nx], c);
            }
        }
    }

    private int[] dx = {0, 1, 0, -1};
    private int[] dy = {1, 0, -1, 0};

    private void visit(Cell cell, Chain chain, ChainColor chainColor) {
        if (cell.chain != null) return;

        cell.chain = chain;
        chain.add(cell);

        for (int i = 0; i < 4; i++) {
            final int nx = cell.x + dx[i];
            final int ny = cell.y + dy[i];
            if (nx >= 0 && ny >= 0 && nx < WIDTH && ny < HEIGHT) {
                final Cell neighbour = grid[ny][nx];
                if (neighbour.eCar != null && chainColor == neighbour.eCar.chainableColor())
                    visit(neighbour, chain, chainColor);
            }
        }
    }

    public E getOccupant(int gridX, int gridY, E e) {
        final int localGridX = gridX - cameraGridOffset;
        if (localGridX >= 0 && gridY >= 0 && localGridX < WIDTH && gridY < HEIGHT) {
            return grid[gridY][localGridX].eCar;
        }
        return null;
    }

    @Override
    protected void process(E e) {

        final int gridX = GridSnapSystem.gridX(e);
        final int gridY = GridSnapSystem.gridY(e);

//        entityWithTag("control-ghost")
//                .posX(gridOffsetX * G.CELL_SIZE).posY(1 * G.CELL_SIZE);

        final int localGridX = gridX - cameraGridOffset;

        if (localGridX >= 0 && gridY >= 0 && localGridX < WIDTH && gridY < HEIGHT) {
            if (e.chainablePitstop()) {
                grid[gridY][localGridX].ePitstop = e;
            } else {
                grid[gridY][localGridX].eCar = e;
            }
        }
    }
}