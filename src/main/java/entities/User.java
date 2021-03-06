package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dtos.UserDTO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String username;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String password;
  @JoinTable(name = "user_roles", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<CoinOrder> orders;

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }

  public User() {}

  public User(UserDTO userDTO) {
    this.username = userDTO.getUsername();
    this.password = BCrypt.hashpw(userDTO.getPassword(),BCrypt.gensalt());
  }

  //TODO Change when password is hashed
   public boolean verifyPassword(String pw){
        return BCrypt.checkpw(pw,this.password);
    }

  public User(String username, String password) {
    this.username = username;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    this.orders = new ArrayList<>();
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String userPass) {
    this.password = userPass;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

  public List<CoinOrder> getOrders() {
    return orders;
  }

  public void addOrders(CoinOrder order) {
    this.orders.add(order);
  }
}
