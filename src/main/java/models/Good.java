package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_GD)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_GD_COUNT_ALL_THIS,
            query = JpaConst.Q_GD_COUNT_ALL_THIS_DEF),
    @NamedQuery(
            name = JpaConst.Q_GD_GET_MY_GOOD,
            query = JpaConst.Q_GD_GET_MY_GOOD_DEF),
    @NamedQuery(
            name = JpaConst.Q_GET_GOOD_EMP,
            query = JpaConst.Q_GET_GOOD_EMP_DEF),
    @NamedQuery(
            name = JpaConst.Q_GD_COUNT_MY_GOOD,
            query = JpaConst.Q_GD_COUNT_MY_GOOD_DEF)
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Good {

    @Id
    @Column(name = JpaConst.GD_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = JpaConst.GD_COL_EMP, nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = JpaConst.GD_COL_REP, nullable = false)
    private Report report;

}
