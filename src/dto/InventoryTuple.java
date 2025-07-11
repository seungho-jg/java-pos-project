package dto;

public class InventoryTuple {
    final private int inventoryId;
    final private long localTick;

    public InventoryTuple(int inventoryId, long localTick) {
        this.inventoryId = inventoryId;
        this.localTick = localTick;
    }
    public int getInventoryId() {
        return inventoryId;
    }

    public long getLocalTick() {
        return localTick;
    }

    public boolean getOverLocalTick(long globalTick){
        return localTick <= globalTick;
    }
}
