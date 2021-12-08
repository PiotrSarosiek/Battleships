package recruitment;

import java.util.List;
import java.util.Optional;

public class Ship {
    int size;
    List<Coords> coordinates;

    public Ship(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public List<Coords> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coords> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean tryDestroySegment(Coords coords){
        Optional<Coords> coordsToRemove = coordinates.stream()
                .filter(segmentCoords -> segmentCoords.getX() == coords.getX() && segmentCoords.getY() == coords.getY())
                .findAny();
        if(coordsToRemove.isPresent()){
            size--;
            return true;
        }
        return false;
    }

    public boolean isDestroyed(){
        return size == 0;
    }
}
