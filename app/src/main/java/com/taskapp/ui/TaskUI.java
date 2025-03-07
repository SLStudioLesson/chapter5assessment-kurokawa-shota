package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import com.taskapp.exception.AppException;

import java.io.InputStreamReader;

import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");

        // ログイン機能
        inputLogin();

        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        // タスク一覧表示
                        taskLogic.showAll(loginUser);
                        // サブメニュー
                        selectSubMenu();
                        break;
                    case "2":
                        // タスク新規登録機能
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        boolean flg = true;
        while (flg) {
            try {
                System.out.print("メールアドレスを入力してください："); // メールアドレス受付
                String email = reader.readLine();

                System.out.print("パスワードを入力してください："); // パスワード受付
                String password = reader.readLine();

                // ログイン処理を呼び出す
                loginUser = userLogic.login(email, password);
                System.out.println();
                flg = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        boolean flg = true;
        while (flg) {
            try {
                // タスクコード入力
                System.out.print("タスクコードを入力してください：");
                String code = reader.readLine();
                if (!isNumeric(code)) {
                    System.out.println("コードは半角の数字で入力してください。");
                    System.out.println();
                    continue;
                }
                int taskCode = Integer.parseInt(code);  // タスクコードを整数に変換
    
                // タスク名入力
                System.out.print("タスク名を入力してください：");
                String name = reader.readLine();
                if (name.length() > 10) {
                    System.out.println("タスク名は10文字以内で入力してください");
                    System.out.println();
                    continue;
                }
    
                // 担当するユーザーコード入力
                System.out.print("担当するユーザーのコードを選択して下さい：");
                String userCode = reader.readLine();
                if (!isNumeric(userCode)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                int repUserCode = Integer.parseInt(userCode);  // ユーザーコードを整数に変換


                // タスク情報を保存
                taskLogic.save(taskCode, name, repUserCode, loginUser);
                flg = false;  // ループ終了
    
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }


    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        boolean flg = true;
    while (flg) {
        try {
            // サブメニュー表示
            System.out.println("以下1~2のメニューから好きな選択肢を選んでください。");
            System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
            System.out.print("選択肢：");
            String selectMenu = reader.readLine();
            
            // メニュー選択に応じた処理
            switch (selectMenu) {
                case "1":
                    // ステータス変更メニューに移動
                    inputChangeInformation();
                    flg = false;  // 処理が完了したらループを終了
                    break;
                case "2":
                    break;
                default:
                    // 無効な選択肢が入力された場合、再度入力を求める
                    System.out.println("選択肢が誤っています。1または2を選択してください。");
                    break;
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
    boolean flg = true;
    while (flg) {
        try {
            // ステータスを変更するタスクコード入力
            System.out.print("ステータスを変更するタスクコードを入力してください：");
            String code = reader.readLine();
            if (!isNumeric(code)) {
                System.out.println("コードは半角の数字で入力してください。");
                System.out.println();
                continue;
            }
            int changeTaskCode = Integer.parseInt(code);  // タスクコードを整数に変換

            // ステータスを変更する選択肢を表示
            System.out.println("どのステータスに変更するか選択してください。");
            System.out.println("1. 着手中, 2. 完了");
            System.out.print("選択肢：");
            String statusInput = reader.readLine();
            System.out.println();
            if (!isNumeric(statusInput)) {
                System.out.println("ステータスは半角の数字で入力してください");
                continue;
            }

            // ステータスを整数に変換
            int status = Integer.parseInt(statusInput);

            // ステータスが1または2であることを確認
            if (status != 1 && status != 2) {
                System.out.println("ステータスは1・2の中から選択してください");
                continue;
            }

            // changeStatusメソッドを呼び出してステータス変更
            taskLogic.changeStatus(changeTaskCode, status, loginUser);

            System.out.println("ステータスの変更が完了しました。");
            flg = false;  // 処理が完了したらループを終了
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AppException e) {
            System.out.println(e.getMessage());
        System.out.println();
        }
    }
}
    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        try {
            Integer.parseInt(inputText);  // 文字列を整数に変換してみる
            return true;  // 変換できた場合、数字である
        } catch (NumberFormatException e) {
            return false;  // 変換できなければ、数字でない
        }
    }
}