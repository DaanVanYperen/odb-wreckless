package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.ChainColor;
import net.mostlyoriginal.game.component.Chainable;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.system.GridSnapSystem;
import net.mostlyoriginal.game.system.TowedSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * @author Daan van Yperen
 */
public class ChainingSystem extends FluidIteratingSystem {

    private static final int WIDTH = 20; // base + 1 for easier scanning
    private static final int HEIGHT = 12; // base + 1 for easier scanning/
    private static final int MAX_CHAINS = WIDTH * HEIGHT; // too many but safe. :)
    private int MAX_CHAIN_LENGTH = 10;

    private GridSnapSystem gridSnapSystem;
    private final Cell[][] grid = new Cell[HEIGHT][WIDTH];
    private final Chain[] chains = new Chain[MAX_CHAINS];
    private int activeChains = 0;

    class Chain {
        public int length;
        public final Cell[] cells = new Cell[MAX_CHAIN_LENGTH];

        void reset() {
            length = 0;
        }

        public void add(Cell cell) {
            cells[length++] = cell;
        }
    }

    class Cell {
        E e;
        Chain chain;
        int x;
        int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void reset() {
            e = null;
            chain = null;
        }
    }

    public ChainingSystem() {
        super(Aspect.all(Chainable.class, Pos.class, Bounds.class));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }
        for (int c = 0; c < MAX_CHAINS; c++) {
            chains[c] = new Chain();
        }

    }

    @Override
    protected void begin() {
        super.begin();
    }

    private void resetChains() {
        for (int c = 0; c < activeChains; c++) {
            chains[c].reset();
        }
        activeChains = 0;
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
        terminateLongChains();

        resetChains();
        resetGrid();
    }

    private void terminateLongChains() {
        for (int c = 0; c < activeChains; c++) {
            final Chain chain = chains[c];
            if (chain.length >= 3) {
                for (int i = 0; i < chain.length; i++) {
                    prepareForReward(chain.cells[i].e
                            .cashableChainLength(chain.length), chain.cells[i]);
                }
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
                if (cell.e != null && cell.chain == null) {
                    visit(cell, chains[activeChains++], cell.e.chainableColor());
                }
            }
        }
    }

    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};

    private void visit(Cell cell, Chain chain, ChainColor chainColor) {
        if (cell.chain != null) return;

        cell.chain = chain;
        chain.add(cell);

        for (int i = 0; i < 4; i++) {
            final int nx = cell.x + dx[i];
            final int ny = cell.y + dy[i];
            if (nx >= 0 && ny >= 0 && nx < WIDTH && ny < HEIGHT) {
                final Cell neighbour = grid[ny][nx];
                if (neighbour.e != null && chainColor == neighbour.e.chainableColor())
                    visit(neighbour, chain, chainColor);
            }
        }
    }

    @Override
    protected void process(E e) {
        if (e.chainablePitstop()) return; // we don't deal with pitstops yet.

        final int gridX = GridSnapSystem.gridX(e);
        final int gridY = GridSnapSystem.gridY(e);

        final int gridOffsetX = GridSnapSystem.gridX(entityWithTag("camera")) - 3;

        entityWithTag("control-ghost")
                .posX(gridOffsetX * G.CELL_SIZE).posY(1 * G.CELL_SIZE);


        final int localGridX = gridX - gridOffsetX;

        if (localGridX >= 0 && gridY >= 0 && localGridX < WIDTH && gridY < HEIGHT) {
            grid[gridY][localGridX].e = e;
            //e.tint(MathUtils.random(0,1f),1f,1f,1f);
        }
    }
}
