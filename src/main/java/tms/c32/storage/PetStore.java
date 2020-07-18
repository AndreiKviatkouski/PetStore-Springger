package tms.c32.storage;

import org.springframework.stereotype.Component;
import tms.c32.entity.Category;
import tms.c32.entity.Pet;
import tms.c32.entity.Status;
import tms.c32.entity.Tags;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PetStore {
    private final static String URL_TABLES = "jdbc:postgresql://localhost:5432/petstore";
    private final static String LOGIN_TABLES = "postgres";
    private final static String PASS_TABLES = "Andrei3400";
    Connection connection = null;

    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean addPet(Pet pet) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into pet (id, name, status) values (?, ?, ?) ");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setString(2, pet.getName());
            preparedStatement.setString(3, pet.getStatus().toString());
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addDataCategory(Pet pet) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into data_category (idpet, idcategory) values (?, ?) ");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setInt(2, pet.getCategory().getId());
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addDataTags(Pet pet, Integer[] array) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into data_tags (idpet, idtag) values (?, ?) ");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setArray(2, connection.createArrayOf("integer", array));
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePetById(Pet pet, int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("update pet set id = ?, name = ?, status = ? where id = ?");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setString(2, pet.getName());
            preparedStatement.setString(3, pet.getStatus().toString());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateNameAndStatusById(int id, String name, Status status) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("update pet set name = ?, status = ? where id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, status.toString());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategoryById(Pet pet, int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("update data_category set idpet = ?, idcategory = ? where idpet = ?");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setInt(2, pet.getCategory().getId());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTagsById(Pet pet, Integer[] array, int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("update data_tags set idpet = ?, idtag = ? where idpet = ?");
            preparedStatement.setInt(1, pet.getId());
            preparedStatement.setArray(2, connection.createArrayOf("integer", array));
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Pet getPetById(int id) {
        try {
            String name = "";
            Status status = null;
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from pet where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            name = resultSet.getString(2);
            status = (Status.valueOf(resultSet.getString(3)));
            Category categoryByIdPet = getCategoryByIdPet(id);
            List<Tags> tagsByIdPet = getTagsByIdPet(id);
            connection.close();
            return new Pet(id, categoryByIdPet, name, tagsByIdPet, status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pet> getPetFindByStatus(Status status) {
        List<Pet> list = new ArrayList<>();
        try {
            int id = 0;
            String name = "";
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from pet where status = ?");
            preparedStatement.setString(1, status.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                name = resultSet.getString(2);
                Category categoryByIdPet = getCategoryByIdPet(id);
                List<Tags> tagsByIdPet = getTagsByIdPet(id);
                Pet pet = new Pet(id, categoryByIdPet, name, tagsByIdPet, status);
                list.add(pet);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Category getCategoryByIdPet(int idPet) {
        try {
            int idCategory = 0;
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from data_category where idpet = ?");
            preparedStatement.setInt(1, idPet);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            idCategory = resultSet.getInt(2);
            Category categoryById = getCategoryById(idCategory);
            connection.close();
            return categoryById;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Category getCategoryById(int id) {
        String name = "";
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from category where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            name = resultSet.getString(2);
            Category category = new Category(id, name);
            connection.close();
            return category;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Tags> getTagsByIdPet(int idPet) {
        List<Tags> tagsList = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from data_tags where idpet = ?");
            preparedStatement.setInt(1, idPet);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Array array = resultSet.getArray(2);
            Integer[] arrayArray = (Integer[]) array.getArray();
            for (int idTags : arrayArray) {
                Tags tagById = getTagById(idTags);
                tagsList.add(tagById);
            }
            connection.close();
            return tagsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Tags getTagById(int id) {
        try {
            String name = "";
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from tag where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            name = resultSet.getString(2);
            Tags tags = new Tags(id, name);
            connection.close();
            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removePetById(int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from pet where id = ? ");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDataCategoryById(int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from data_category where idpet = ? ");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDataTagById(int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from data_tags where idpet = ? ");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPet(int id) {
        try {
            connection = DriverManager.getConnection(URL_TABLES, LOGIN_TABLES, PASS_TABLES);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from pet  where id = ? ");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean next = resultSet.next();
            connection.close();
            return next;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
