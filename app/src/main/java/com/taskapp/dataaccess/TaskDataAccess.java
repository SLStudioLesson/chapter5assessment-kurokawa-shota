package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<Task>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // CSVに間違いがあったらスキップする
                if (values.length != 4) {
                    continue;
                }

                int code = Integer.parseInt(values[0]);
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                User repUser = userDataAccess.findByCode(Integer.parseInt(values[3]));

                // Taskオブジェクトにマッピング
                Task task = new Task(code, name, status, repUser);
                // taskに追加
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // タスク情報をCSVフォーマットで書き込む
        writer.write(task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode());
        writer.newLine(); // 新しい行を追加
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int code) {
        Task task= null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            //タイトル行を読み飛ばす
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int taskCode = Integer.parseInt(values[0]);
                if(code != taskCode) continue;
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                int repUserCode = Integer.parseInt(values[3]);
                //taskオブジェクトにマッピングしていく
                task = new Task(repUserCode, name, status, userDataAccess.findByCode(repUserCode));
                break;
            }
        } catch (IOException e) {
            e.printStackTrace(); // 書き込みエラーがあった場合、スタックトレースを出力
        }
        return task;
    }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = new ArrayList<>();
    boolean taskUpdated = false;

    // まず既存のタスクを読み込み
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        br.readLine(); // ヘッダー行をスキップ
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            int taskCode = Integer.parseInt(data[0]);
            String taskName = data[1];
            int status = Integer.parseInt(data[2]);
            int userCode = Integer.parseInt(data[3]);

            if (taskCode == updateTask.getCode()) {
                taskName = updateTask.getName();
                status = updateTask.getStatus();
                userCode = updateTask.getRepUser().getCode();
                taskUpdated = true;
            }

            Task taskFromFile = new Task(taskCode, taskName, status, userDataAccess.findByCode(userCode));
            tasks.add(taskFromFile);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // タスクが更新されていない場合は新たにリストに追加
    if (!taskUpdated) {
        tasks.add(updateTask);
    }

    // ファイルを上書きして新しいタスクのリストを書き込む
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
        // ヘッダーを書き込む
        bw.write("コード,タスク名,ステータス,担当者コード");
        bw.newLine();
        for (Task t : tasks) {
            bw.write(t.getCode() + "," + t.getName() + "," + t.getStatus() + "," + t.getRepUser().getCode());
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }
}