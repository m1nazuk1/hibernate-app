package org.example.dao;

import org.example.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select p from Person p", Person.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person personToBeUpdated = session.get(Person.class, id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Person.class, id));
    }

//
//    //todo: batch update (для работы с большим количеством запросов 1000+)
//
//    public void testMultipleUpdate(){
//        List<Person> people = create1000people();
//
//        long before = System.currentTimeMillis();
//
//        for (Person person: people) {
//            jdbcTemplate.update("INSERT INTO Person(name, age, email, address) VALUES(?, ?, ?, ?)",person.getId(), person.getName(), person.getAge(),
//                    person.getEmail(), person.getAddress());
//        }
//
//        long after = System.currentTimeMillis();
//
//        System.out.println("Time: " + (after-before));
//    }
//
//    public void testBatchUpdate(){
//        List<Person> people = create1000people();
//
//        long before = System.currentTimeMillis();
//
//        jdbcTemplate.batchUpdate("INSERT INTO Person(name, age, email) VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
//                preparedStatement.setString(1, people.get(i).getName());
//                preparedStatement.setInt(2, people.get(i).getAge());
//                preparedStatement.setString(3, people.get(i).getEmail());
//                preparedStatement.setString(4, people.get(i).getAddress());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return people.size();
//            }
//        });
//
//        long after = System.currentTimeMillis();
//
//        System.out.println("Time: " + (after-before));
//
//    }
//
//    private List<Person> create1000people() {
//        List<Person> people = new ArrayList<>();
//
//        for (int i = 0; i < 1000; i++){
//            people.add(new Person(i ,"Name" + i, 30, "test" + i + "@mail.ru", "some address"));
//        }
//        return people;
//    }
}