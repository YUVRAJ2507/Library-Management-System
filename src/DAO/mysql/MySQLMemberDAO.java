package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import dao.MemberDAO;
import model.Member;
public class MySQLMemberDAO implements MemberDAO {

    @Override
    public void addMember(Member member) throws SQLException {

        String sql = "INSERT INTO members (member_id, name, phone) "+ "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, member.getMemberId());
            statement.setString(2, member.getName());
            statement.setString(3, member.getPhone());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected != 1) {
                throw new SQLException("Member could not be added.");
            }
        }
    }

    @Override
    public List<Member> getAllMembers() throws SQLException {

        List<Member> members = new ArrayList<>();
        String sql = "SELECT member_id, name, phone FROM members";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int memberId = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");

                Member member = new Member(memberId, name, phone);
                members.add(member);
            }
        }

        return members;
    }
}
