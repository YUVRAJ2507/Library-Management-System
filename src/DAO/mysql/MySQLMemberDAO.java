package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import config.DatabaseConnection;
import dao.MemberDAO;
import model.Member;

public class MySQLMemberDAO implements MemberDAO {

        @Override
        public void addMember(Member member) {

                String sql = "INSERT INTO members " +
                                "(member_id, name, phone) " +
                                "VALUES (?, ?, ?)";

                try (Connection connection = DatabaseConnection.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setInt(1, member.getMemberId());
                        statement.setString(2, member.getName());
                        statement.setString(3, member.getPhone());
                        statement.executeUpdate();

                        System.out.println("Member added to database successfully.");
                } catch (Exception e) {

                        System.out.println("Failed to add member.");
                        e.printStackTrace();
                }
        }

        @Override
        public ArrayList<Member> getAllMembers() {

                ArrayList<Member> members = new ArrayList<>();
                String sql = "SELECT * FROM members";
                try (
                                Connection connection = DatabaseConnection.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql);
                                ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {

                                int memberId = resultSet.getInt("member_id");
                                String name = resultSet.getString("name");
                                String phone = resultSet.getString("phone");
                                Member member = new Member(memberId, name, phone);
                                members.add(member);
                        }

                } catch (Exception e) {
                        System.out.println("Failed to retrieve members.");
                        e.printStackTrace();
                }

                return members;
        }
}