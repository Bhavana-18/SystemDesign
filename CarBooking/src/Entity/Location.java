package Entity;

public record Location(double latitude, double longitude) {
    public double distanceTo(Location other){
        double latDiff = this.latitude - other.latitude;
        double lonDiff = this.longitude - other.longitude;
        return Math.sqrt(latDiff*latDiff + lonDiff * lonDiff);
    }
}
