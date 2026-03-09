package Service;

import Entity.Driver;
import Entity.Location;

import java.util.List;

public interface MatchingStrategy {
    Driver findBestDriver(Location pickup, List<Driver> drivers);
}
