package helper;

import DAO.DivisionDao;
import DAO.DivisionDaoImpl;
import javafx.collections.ObservableList;
import model.Division;

public class ListManager {
    public static ObservableList<Division> getFilteredDivisions(int countryID){
        DivisionDao divisionDao = new DivisionDaoImpl();
        return divisionDao.getDivisionsByCountry(countryID);
    }
}
