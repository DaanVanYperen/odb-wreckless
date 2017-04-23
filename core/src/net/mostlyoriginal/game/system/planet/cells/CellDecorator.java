package net.mostlyoriginal.game.system.planet.cells;

import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.Planet;
import net.mostlyoriginal.game.component.PlanetCell;
import net.mostlyoriginal.game.component.StatusMask;

/**
 * @author Daan van Yperen
 */
public class CellDecorator {
    public PlanetCell cell;
    public final Planet planet;
    public CellDecorator helper;

    public CellDecorator(Planet planet) {
        this.planet = planet;
    }

    public void setColor(int color) {
        cell.color = color;
    }

    public CellDecorator proxies(PlanetCell cell) {
        this.cell = cell;
        return this;
    }

    public CellDecorator forChild(PlanetCell cell) {
        if (helper == null) {
            helper = new CellDecorator(planet);
        }
        return helper.proxies(cell);
    }

    public PlanetCell getNeighbourAbove() {
        return planet.get(cell.x + PlanetCell.directions[cell.up()][0], cell.y + PlanetCell.directions[cell.up()][1]);
    }

    public PlanetCell getNeighbourLeft() {
        return planet.get(cell.x + PlanetCell.directions[cell.left()][0], cell.y + PlanetCell.directions[cell.left()][1]);
    }

    public PlanetCell getNeighbourDown() {
        return cell.down != -1 ? planet.get(cell.x + PlanetCell.directions[cell.down][0], cell.y + PlanetCell.directions[cell.down][1]) : null;
    }

    public PlanetCell getNeighbourRight() {
        return planet.get(cell.x + PlanetCell.directions[cell.right()][0], cell.y + PlanetCell.directions[cell.right()][1]);
    }

    public PlanetCell getNeighbourAboveL() {
        return planet.get(cell.x + PlanetCell.directions[cell.upL()][0], cell.y + PlanetCell.directions[cell.upR()][1]);
    }

    public PlanetCell getNeighbourAboveR() {
        return planet.get(cell.x + PlanetCell.directions[cell.upR()][0], cell.y + PlanetCell.directions[cell.upL()][1]);
    }

    public PlanetCell getBelow() {
        return planet.get(cell.x + PlanetCell.directions[cell.down][0], cell.y + PlanetCell.directions[cell.down][1]);
    }

    public PlanetCell getNeighbour(PlanetCell.CellType type) {
        for (int i = 0; i < 8; i++) {
            final PlanetCell result = planet.get(cell.x + PlanetCell.directions[i][0], cell.y + PlanetCell.directions[i][1]);
            if (result != null && result.type == type) return result;
        }
        return null;
    }

    public void setNextType(PlanetCell.CellType type) {
        cell.nextType = type;
    }

    public int countNeighbour(PlanetCell.CellType type) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            final PlanetCell result = planet.get(cell.x + PlanetCell.directions[i][0], cell.y + PlanetCell.directions[i][1]);
            if (result != null && result.type == type) count++;
        }
        return count;
    }

    public int countNonLavaNeighbour() {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            final PlanetCell result = planet.get(cell.x + PlanetCell.directions[i][0], cell.y + PlanetCell.directions[i][1]);
            if (result != null && (result.type != PlanetCell.CellType.LAVA_CRUST && result.type != PlanetCell.CellType.LAVA))
                count++;
        }
        return count;
    }

    private PlanetCell temp = new PlanetCell();

    public void swapWith(PlanetCell target) {
        if (target.nextType == null) {
            target.nextType = cell.type;
            target.nextColor = cell.color;
            cell.nextType = target.type;
            cell.nextColor = target.color;
        }
    }


    public boolean swapWithBestFlowing(PlanetCell target) {
        if (target != null && target.type != cell.type && target.type.flows() && target.type.density < cell.type.density) {
            final PlanetCell belowTarget = forChild(target).getNeighbourDown();
            if (belowTarget != null && belowTarget.type.flows() && belowTarget.type.density < cell.type.density) {
                swapWith(belowTarget);
                return true;
            } else {
                swapWith(target);
                return true;
            }
        }
        return false;
    }


    public StatusMask mask() {
        return planet.getStatusMask(cell.x, cell.y);
    }

    public void flow() {
        if (MathUtils.randomBoolean()) {
            swapWithBestFlowing(getNeighbourLeft());
        } else {
            swapWithBestFlowing(getNeighbourRight());
        }
    }
}
