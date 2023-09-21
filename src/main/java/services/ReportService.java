package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.GoodConverter;
import actions.views.GoodView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Good;
import models.Report;
import models.validators.ReportValidator;

public class ReportService extends ServiceBase {

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMinePerPage(EmployeeView employee, int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
                                 .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                                 .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                                 .setMaxResults(JpaConst.ROW_PER_PAGE)
                                 .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 指定した従業員が作成した日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する日報データを取得し、ReportViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getAllPerPage(int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 日報テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;
    }

    /**
     * idを条件に取得したデータをReportViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、日報テーブルに登録する
     * @param rv 日報の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(ReportView rv) {
       List<String> errors = ReportValidator.validate(rv);
       if (errors.size() == 0) {
           LocalDateTime ldt = LocalDateTime.now();
           rv.setCreatedAt(ldt);
           rv.setUpdatedAt(ldt);
           createInternal(rv);
       }

       //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
       return errors;
    }

    /**
     * 画面から入力された日報の登録内容を元に、日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView rv) {

        //バリデーションを行う
        List<String> errors = ReportValidator.validate(rv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    /**
     * 日報データを1件登録する
     * @param rv 日報データ
     */
    private void createInternal(ReportView rv) {

        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();
    }

    /**
     * 日報データを更新する
     * @param rv 日報データ
     */
    private void updateInternal(ReportView rv) {

        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();
    }

    //いいねを登録する
    public void createGd(GoodView gv) {
        em.getTransaction().begin();
        em.persist(GoodConverter.toModel(gv));
        em.getTransaction().commit();
    }

    //指定の日報IDを持つ日報についたいいね件数を全件取得
    public long countAllThis(ReportView report) {

        long count = (long) em.createNamedQuery(JpaConst.Q_GD_COUNT_ALL_THIS, Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                .getSingleResult();

        return count;
    }

    //指定の従業員が指定の日報につけたいいねの件数を取得
    public long countMyGood(ReportView report, EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_GD_COUNT_MY_GOOD, Long.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }



    //指定の日報にいいねした従業員のリストを取得
    public List<EmployeeView> getGoodEmp(ReportView report) {

        List<Employee> employees = em.createNamedQuery(JpaConst.Q_GET_GOOD_EMP, Employee.class)
                            .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                            .setMaxResults(5)
                            .getResultList();
        return EmployeeConverter.toViewList(employees);
    }

    //指定の従業員が指定の日報につけたいいねのデータを取得
    public List<GoodView> getMyGood(ReportView report, EmployeeView employee) throws NullPointerException {
        List<Good> good = em.createNamedQuery(JpaConst.Q_GD_GET_MY_GOOD, Good.class)
                .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getResultList();

        return GoodConverter.toViewList(good);
    }


    //指定のいいねデータを削除
    public void destroyGd(GoodView gv) {

        Good mg = em.find(Good.class, gv.getId());

        em.getTransaction().begin();
        em.remove(mg);
        em.getTransaction().commit();

    }

    //

}
