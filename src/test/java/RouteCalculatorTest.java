import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorTest extends TestCase {
    StationIndex stationIndex;
    RouteCalculator calculator;
    List<Station> actualRoute;
    List<Station> expectedRoute;
    List<Station> connectionStations;

    @Override
    protected void setUp() throws Exception {
        stationIndex = new StationIndex();
        calculator = new RouteCalculator(stationIndex);
        actualRoute = new ArrayList<>();
        expectedRoute = new ArrayList<>();
        connectionStations = new ArrayList<>();

        Line line1 = new Line(1, "First");
        Line line2 = new Line(2, "Second");
        Line line3 = new Line(3, "Third");

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        line1.addStation(new Station("Petrovskaya", line1));
        line1.addStation(new Station("Arbuznaya", line1));
        line1.addStation(new Station("Dunayskaya", line1));
        line2.addStation(new Station("Morkovnaya", line2));
        line2.addStation(new Station("Yablochnaya", line2));
        line3.addStation(new Station("Persikovaya", line3));
        line3.addStation(new Station("Khibinskaya", line3));

        stationIndex.addStation(new Station("Petrovskaya", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Arbuznaya", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Dunayskaya", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Morkovnaya", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Yablochnaya", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Persikovaya", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("Khibinskaya", stationIndex.getLine(3)));

        connectionStations.add(stationIndex.getStation("Arbuznaya"));
        connectionStations.add(stationIndex.getStation("Morkovnaya"));
        stationIndex.addConnection(connectionStations);
        connectionStations.clear();

        connectionStations.add(stationIndex.getStation("Yablochnaya", stationIndex.getLine(2).getNumber()));
        connectionStations.add(stationIndex.getStation("Persikovaya", stationIndex.getLine(3).getNumber()));
        stationIndex.addConnection(connectionStations);
        connectionStations.clear();
    }

    public void testGetShortestRoute() {
        actualRoute = calculator.getShortestRoute(stationIndex.getStation("Dunayskaya"), stationIndex.getStation("Khibinskaya"));

        expectedRoute.add(stationIndex.getStation("Dunayskaya"));
        expectedRoute.add(stationIndex.getStation("Arbuznaya"));
        expectedRoute.add(stationIndex.getStation("Morkovnaya"));
        expectedRoute.add(stationIndex.getStation("Yablochnaya"));
        expectedRoute.add(stationIndex.getStation("Persikovaya"));
        expectedRoute.add(stationIndex.getStation("Khibinskaya"));

        assertEquals(expectedRoute, actualRoute);
    }

    public void testCalculateDuration() {
        actualRoute.add(stationIndex.getStation("Petrovskaya"));
        actualRoute.add(stationIndex.getStation("Arbuznaya"));
        actualRoute.add(stationIndex.getStation("Morkovnaya"));
        actualRoute.add(stationIndex.getStation("Yablochnaya"));

        double actual = RouteCalculator.calculateDuration(actualRoute);
        double expected = 8.5;
        assertEquals(expected, actual);
    }

    public void testGetRouteOnTheLine() {
        actualRoute = calculator.getShortestRoute(stationIndex.getStation("Petrovskaya"), stationIndex.getStation("Dunayskaya"));

        expectedRoute.add(stationIndex.getStation("Petrovskaya"));
        expectedRoute.add(stationIndex.getStation("Arbuznaya"));
        expectedRoute.add(stationIndex.getStation("Dunayskaya"));

        assertEquals(expectedRoute, actualRoute);
    }

    public void testGetRouteWithOneConnection() {
        actualRoute = calculator.getShortestRoute(stationIndex.getStation("Petrovskaya"), stationIndex.getStation("Yablochnaya"));

        expectedRoute.add(stationIndex.getStation("Petrovskaya"));
        expectedRoute.add(stationIndex.getStation("Arbuznaya"));
        expectedRoute.add(stationIndex.getStation("Morkovnaya"));
        expectedRoute.add(stationIndex.getStation("Yablochnaya"));

        assertEquals(expectedRoute, actualRoute);
    }

    public void testGetRouteWithTwoConnections() {
        actualRoute = calculator.getShortestRoute(stationIndex.getStation("Petrovskaya"), stationIndex.getStation("Khibinskaya"));

        expectedRoute.add(stationIndex.getStation("Petrovskaya"));
        expectedRoute.add(stationIndex.getStation("Arbuznaya"));
        expectedRoute.add(stationIndex.getStation("Morkovnaya"));
        expectedRoute.add(stationIndex.getStation("Yablochnaya"));
        expectedRoute.add(stationIndex.getStation("Persikovaya"));
        expectedRoute.add(stationIndex.getStation("Khibinskaya"));

        assertEquals(expectedRoute, actualRoute);
    }

    public void testIsConnected() {
        actualRoute = calculator.getShortestRoute(stationIndex.getStation("Arbuznaya"), stationIndex.getStation("Morkovnaya"));

        expectedRoute.add(stationIndex.getStation("Arbuznaya"));
        expectedRoute.add(stationIndex.getStation("Morkovnaya"));

        assertEquals(expectedRoute, actualRoute);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
