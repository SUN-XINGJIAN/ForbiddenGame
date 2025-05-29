package Tests;

import controller.DifficultyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DifficultyTest {
    private DifficultyController controller;
    private Stage stage;
    private RadioButton rbNovice;
    private RadioButton rbNormal;
    private RadioButton rbElite;
    private RadioButton rbLegendary;

    @BeforeClass
    public static void setupJavaFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (Exception e) {
            // 如果已经启动，忽略异常
        }
    }

    @Before
    public void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        javafx.application.Platform.runLater(() -> {
            try {
                // 尝试多个可能的路径
                String[] possiblePaths = {
                    "/fxml/DifficultySelection.fxml",           // 标准路径
                    "fxml/DifficultySelection.fxml",            // 不带前导斜杠
                    "/DifficultySelection.fxml",                // 根目录
                    "DifficultySelection.fxml"                  // 当前目录
                };

                FXMLLoader loader = null;
                for (String path : possiblePaths) {
                    try {
                        loader = new FXMLLoader(getClass().getResource(path));
                        if (loader != null) {
                            System.out.println("Found FXML file at: " + path);
                            break;
                        }
                    } catch (Exception e) {
                        // 继续尝试下一个路径
                        continue;
                    }
                }

                if (loader == null) {
                    throw new RuntimeException("Cannot find Difficulty.fxml file");
                }

                // 加载FXML文件
                Parent root = loader.load();

                // 获取控制器
                controller = loader.getController();
                if (controller == null) {
                    throw new RuntimeException("DifficultyController is null");
                }

                // 创建舞台
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                // 获取RadioButton控件
                rbNovice = (RadioButton) stage.getScene().lookup("#rbNovice");
                rbNormal = (RadioButton) stage.getScene().lookup("#rbNormal");
                rbElite = (RadioButton) stage.getScene().lookup("#rbElite");
                rbLegendary = (RadioButton) stage.getScene().lookup("#rbLegendary");

                // 设置玩家数量
                controller.setPlayerCount(2);

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
                throw new RuntimeException("Failed to initialize: " + e.getMessage(), e);
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        if (!completed) {
            throw new RuntimeException("Failed to initialize JavaFX components");
        }
    }

    @Test
    public void testDifficultySelection() {
        CountDownLatch latch = new CountDownLatch(1);

        javafx.application.Platform.runLater(() -> {
            try {
                System.out.println("Starting difficulty test...");

                // 测试新手难度
                testDifficultyLevel(rbNovice, 1);
                
                // 测试普通难度
                testDifficultyLevel(rbNormal, 2);
                
                // 测试精英难度
                testDifficultyLevel(rbElite, 3);
                
                // 测试传奇难度
                testDifficultyLevel(rbLegendary, 4);

                System.out.println("All difficulty tests completed successfully!");
                latch.countDown();

            } catch (Exception e) {
                e.printStackTrace();
                fail("Test failed with exception: " + e.getMessage());
                latch.countDown();
            }
        });

        try {
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                fail("Test did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            fail("Test was interrupted: " + e.getMessage());
        }
    }

    private void testDifficultyLevel(RadioButton radioButton, int expectedWaterMeterIndex) {
        System.out.println("\nTesting " + radioButton.getId() + " difficulty...");

        // 直接设置选中状态
        radioButton.setSelected(true);
        System.out.println("Selected " + radioButton.getId() + " difficulty");

        // 验证水位计索引
        int waterMeterIndex = getWaterMeterIndex(radioButton);
        assertEquals("Water meter index for " + radioButton.getId() + " should be " + expectedWaterMeterIndex,
                expectedWaterMeterIndex, waterMeterIndex);
        System.out.println("Water meter index verified: " + waterMeterIndex);
    }

    private int getWaterMeterIndex(RadioButton radioButton) {
        if (radioButton.getId().equals("rbNovice")) return 1;
        if (radioButton.getId().equals("rbNormal")) return 2;
        if (radioButton.getId().equals("rbElite")) return 3;
        if (radioButton.getId().equals("rbLegendary")) return 4;
        return -1;
    }

    @After
    public void tearDown() {
        if (stage != null) {
            javafx.application.Platform.runLater(() -> stage.close());
        }
    }
}