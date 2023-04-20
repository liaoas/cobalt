package com.liao.book.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import com.liao.book.common.ModuleConstants;
import com.liao.book.core.Convert;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.enums.ToastType;
import com.liao.book.factory.BeanFactory;
import com.liao.book.persistence.ReadSubscriptDao;
import com.liao.book.persistence.ReadingProgressDao;
import com.liao.book.persistence.SettingsDao;
import com.liao.book.service.ChapterService;
import com.liao.book.service.ContentService;
import com.liao.book.service.SearchService;
import com.liao.book.service.impl.ChapterServiceImpl;
import com.liao.book.service.impl.ContentServiceImpl;
import com.liao.book.service.impl.SearchServiceImpl;
import com.liao.book.utils.ModuleUtils;
import com.liao.book.utils.ReadingUtils;
import com.liao.book.utils.ToastUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 首页窗口
 * </p>
 *
 * @author LiAo
 * @since 2021/1/14
 */
public class MainUI {
    // 搜索按钮
    private JButton btnSearch;

    // 搜索文本框
    private JTextField textSearchBar;

    // 窗口
    private JPanel mainPanel;

    // 开始阅读按钮
    private JButton openBook;

    // 搜索书本新信息
    private JTable searchBookTable;

    // 上一章按钮
    private JButton btnOn;

    // 下一章按钮
    private JButton underOn;

    // 章节跳转按钮
    private JButton jumpButton;

    // 章节内容
    private JTextArea textContent;

    // 章节内容外部框
    private JScrollPane paneTextContent;

    // 章节目录下拉列表
    private JComboBox<String> chapterList;

    // 表格外围
    private JScrollPane tablePane;

    // 搜索下拉列表数据源
    private JComboBox<String> sourceDropdown;

    // 设置按钮
    private JButton settingBtn;

    // 全局模块对象
    private final Project project;

    // 搜索书籍名称
    private String bookSearchName;

    // 书籍链接
    private String valueAt;

    // 用于判断是否是当前选项卡切换
    private Content lastSelectedContent = null;

    // 是否切换了书本（是否点击了开始阅读按钮）
    public static boolean isReadClick = false;

    // 书籍爬虫
    static SearchService searchService = (SearchServiceImpl) BeanFactory.getBean("SearchServiceImpl");

    // 章节爬虫
    static ChapterService chapterService = (ChapterServiceImpl) BeanFactory.getBean("ChapterServiceImpl");

    // 内容爬虫
    static ContentService textService = (ContentServiceImpl) BeanFactory.getBean("ContentServiceImpl");


    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    // 阅读窗口滚动位置持久化
    static ReadSubscriptDao readSubscriptDao = ReadSubscriptDao.getInstance();

    // 页面设置持久化
    static SettingsDao settingDao = SettingsDao.getInstance();

    // 初始化数据
    private void init() {
        // 初始化表格
        searchBookTable.setModel(ModuleConstants.tableModel);
        searchBookTable.setEnabled(true);

        // 加载数据源下拉框
        for (String dataSourceName : ModuleConstants.DATA_SOURCE) {
            sourceDropdown.addItem(dataSourceName);
        }

        // 设置表格内容大小
        tablePane.setPreferredSize(new Dimension(-1, 30));

        // 加载组件配置信息
        ModuleUtils.loadModuleConfig(paneTextContent);

        // 加载提示信息
        ModuleUtils.loadComponentTooltip(btnSearch, openBook, settingBtn, btnOn, underOn, jumpButton);

        // 加载阅读进度
        ReadingUtils.loadReadingProgress(chapterList, textContent);

        // 加载持久化的设置
        ModuleUtils.loadSetting(paneTextContent, textContent);
    }

    // 页面初始化加载
    public MainUI(Project project, ToolWindow toolWindow) {
        this.project = project;
        // 执行初始化表格
        init();

        // 搜索
        btnSearch.addActionListener(e -> {

            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);
            // 清空表格数据
            ModuleConstants.tableModel.setRowCount(0);
            // 获取搜索输入文本
            bookSearchName = textSearchBar.getText();

            if (bookSearchName == null || bookSearchName.equals("")) {
                ToastUtils.showToastMassage(project, "请输入书籍名称", ToastType.ERROR);
                // 等待鼠标样式
                ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 获取数据源类型
            instance.searchType = Objects.requireNonNull(sourceDropdown.getSelectedItem()).toString();

            // 重置 重试次数
            SearchServiceImpl.index = 2;

            // 根据数据源类型 搜索
            new SearchBooks().execute();
        });

        // 开始阅读
        openBook.addActionListener(e -> {

            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);

            // 获取选中行数据
            int selectedRow = searchBookTable.getSelectedRow();

            if (selectedRow < 0) {
                ToastUtils.showToastMassage(project, "还没有选择要读哪本书", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 获取书籍链接
            valueAt = searchBookTable.getValueAt(selectedRow, 4).toString();

            // 重置重试次数
            ChapterServiceImpl.index = 2;

            // 执行开始阅读
            new StartReading().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 上一章节跳转
        btnOn.addActionListener(e -> {
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);

            if (instance.chapters.size() == 0 || instance.nowChapterIndex == 0) {
                ToastUtils.showToastMassage(project, "已经是第一章了", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                return;
            }
            instance.nowChapterIndex = instance.nowChapterIndex - 1;
            // 加载阅读信息
            new LoadChapterInformation().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 下一章跳转
        underOn.addActionListener(e -> {
            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);

            if (instance.chapters.size() == 0 || instance.nowChapterIndex == instance.chapters.size() - 1) {
                ToastUtils.showToastMassage(project, "已经是最后一章了", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 章节下标加一
            instance.nowChapterIndex = instance.nowChapterIndex + 1;

            // 加载阅读信息
            new LoadChapterInformation().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 章节跳转
        jumpButton.addActionListener(e -> {
            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);

            // 根据下标跳转
            instance.nowChapterIndex = chapterList.getSelectedIndex();

            if (instance.chapters.size() == 0 || instance.nowChapterIndex < 0) {
                ToastUtils.showToastMassage(project, "未知章节", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 加载阅读信息
            new LoadChapterInformation().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 阅读滚动
        paneTextContent.getVerticalScrollBar().addAdjustmentListener(e -> {
            int textWinIndex = paneTextContent.getVerticalScrollBar().getValue();
            if (!(textWinIndex <= 0)) {
                readSubscriptDao.homeTextWinIndex = textWinIndex;
                // 阅读进度持久化
                readSubscriptDao.loadState(readSubscriptDao);
            }
        });

        // 窗口加载结束
        ApplicationManager.getApplication().invokeLater(() -> {

            paneTextContent.getVerticalScrollBar().setValue(readSubscriptDao.homeTextWinIndex);

            // 窗口未初始化
            if (project.isDisposed() || toolWindow == null) return;

            final ContentManager contentManager = toolWindow.getContentManager();

            // 监听当前选中的面板 进行阅读进度同步
            contentManager.addContentManagerListener(new ContentManagerListener() {
                @Override
                public void selectionChanged(@NotNull ContentManagerEvent event) {
                    Content selectedContent = event.getContent();

                    if (instance.chapters.isEmpty() || selectedContent == lastSelectedContent) return;

                    // 同步字体等设置
                    ModuleUtils.loadSetting(paneTextContent, textContent);

                    ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.WAIT_CURSOR);

                    // 只有选择的内容面板发生变化时才进行相关操作
                    lastSelectedContent = selectedContent;
                    if (selectedContent.getDisplayName().equals(ModuleConstants.TAB_CONTROL_TITLE_HOME)) {
                        // 获取新的章节位置
                        Chapter chapter = instance.chapters.get(instance.nowChapterIndex);

                        // 章节内容赋值
                        textContent.setText(instance.textContent);
                        // 设置下拉框的值
                        chapterList.setSelectedItem(chapter.getName());
                        // 回到顶部
                        textContent.setCaretPosition(1);
                    }
                    ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
                }
            });
            toolWindow.installWatcher(contentManager);
        });

        // 设置单击事件
        settingBtn.addActionListener(e -> {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "com.liao.book.configurable.SettingsConfigurable");
        });
    }

    /**
     * 书籍搜索
     */
    final class SearchBooks extends SwingWorker<Void, List<BookData>> {
        @Override
        protected Void doInBackground() {
            List<BookData> bookData = searchService.getBookNameData(bookSearchName);

            if (bookData == null || bookData.size() == 0) {
                ToastUtils.showToastMassage(project, "没有找到啊", ToastType.ERROR);
                return null;
            }

            //将当前进度信息加入chunks中
            publish(bookData);
            return null;
        }

        @Override
        protected void process(List<List<BookData>> chunks) {
            List<BookData> bookData = chunks.get(0);
            for (BookData bookDatum : bookData) {
                ModuleConstants.tableModel.addRow(Convert.bookData2Array(bookDatum));
            }
        }

        @Override
        protected void done() {
            // 恢复默认鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
        }
    }

    /**
     * 开始阅读
     */
    final class StartReading extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {
            chapterService.getBookChapterByType(valueAt);
            return null;
        }

        @Override
        protected void done() {
            // 清空章节信息
            instance.nowChapterIndex = 0;

            // 清空下拉列表
            chapterList.removeAllItems();

            // 加载下拉列表
            for (Chapter chapter : instance.chapters) {
                chapterList.addItem(chapter.getName());
            }

            // 解析当前章节内容
            new LoadChapterInformation().execute();

            // 书本已切换
            isReadClick = true;
            // 恢复默认鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
        }
    }

    /**
     * 加载章节信息
     */
    final class LoadChapterInformation extends SwingWorker<Void, Chapter> {
        @Override
        protected Void doInBackground() {
            // 清空书本表格
            Chapter chapter = instance.chapters.get(instance.nowChapterIndex);

            // 重置重试次数
            ContentServiceImpl.index = 2;

            // 内容
            textService.searchBookChapterData(chapter.getLink());

            if (instance.textContent == null) {
                ToastUtils.showToastMassage(project, "章节内容为空", ToastType.ERROR);
                return null;
            }

            //将当前进度信息加入chunks中
            publish(chapter);
            return null;
        }

        @Override
        protected void process(List<Chapter> chapters) {
            Chapter chapter = chapters.get(0);
            // 章节内容赋值
            textContent.setText(instance.textContent);
            // 设置下拉框的值
            chapterList.setSelectedItem(chapter.getName());
            // 回到顶部
            textContent.setCaretPosition(1);
        }

        @Override
        protected void done() {
            // 恢复默认鼠标样式
            ModuleUtils.loadTheMouseStyle(mainPanel, Cursor.DEFAULT_CURSOR);
        }
    }

    /**
     * 应用字体大小的修改
     */
    private void applyFontSize() {
        SettingsUI settingsUI = (SettingsUI) BeanFactory.getBean("SettingsUI");
        textContent.setFont(new Font("", Font.BOLD, settingsUI.getFontSizeVal()));
        // 持久化
        settingDao.fontSize = settingsUI.getFontSizeVal();
        settingDao.loadState(settingDao);
    }

    /**
     * 应用滚动速度滑块
     */
    private void applyScrollSpacing() {
        SettingsUI settingsUI = (SettingsUI) BeanFactory.getBean("SettingsUI");
        paneTextContent.getVerticalScrollBar().setUnitIncrement(settingsUI.getReadRollVal());

        // 持久化
        settingDao.scrollSpacingScale = settingsUI.getReadRollVal();
        settingDao.loadState(settingDao);
    }

    /**
     * 书籍导入
     */
    private void applyImportBook() {
        SettingsUI settingsUI = (SettingsUI) BeanFactory.getBean("SettingsUI");

        instance.loadState(instance);
        if (settingsUI.isSelBook) {
            instance.searchType = ModuleConstants.IMPORT;

            instance.importPath = settingsUI.getImportBookPath();

            // 执行开始阅读
            new StartReading().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        }
    }

    /**
     * 页面统一的Apply
     */
    public void apply() {
        // 字体大小
        applyFontSize();

        // 滑块滚动
        applyScrollSpacing();

        // 导入的书籍展示
        applyImportBook();
    }

    // 窗口信息
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
    }

    public JTextArea getTextContent() {
        return textContent;
    }
}

