package com.liao.book.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import com.liao.book.dao.ReadSubscriptDao;
import com.liao.book.dao.ReadingProgressDao;
import com.liao.book.dao.WindowsSettingDao;
import com.liao.book.entity.BookData;
import com.liao.book.entity.Chapter;
import com.liao.book.common.ModuleConstants;
import com.liao.book.enums.ToastType;
import com.liao.book.factory.BeanFactory;
import com.liao.book.service.BookChapterService;
import com.liao.book.service.BookSearchService;
import com.liao.book.service.BookTextService;
import com.liao.book.service.impl.BookChapterServiceImpl;
import com.liao.book.service.impl.BookSearchServiceImpl;
import com.liao.book.service.impl.BookTextServiceImpl;
import com.liao.book.core.Convert;
import com.liao.book.utils.ModuleUtils;
import com.liao.book.utils.ReadingUtils;
import com.liao.book.utils.ToastUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JPanel bookMainJPanel;

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

    // 字体放大
    private JButton fontSizeDown;

    // 字体调小
    private JButton fontSizeUp;

    // 章节目录下拉列表
    private JComboBox<String> chapterList;

    // 表格外围
    private JScrollPane tablePane;

    // 搜索下拉列表数据源
    private JComboBox<String> sourceDropdown;

    // 滚动间距
    private JSlider scrollSpacing;
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
    static BookSearchService searchService = (BookSearchServiceImpl) BeanFactory
            .getBean("BookSearchServiceImpl");

    // 章节爬虫
    static BookChapterService chapterService = (BookChapterServiceImpl) BeanFactory
            .getBean("BookChapterServiceImpl");

    // 内容爬虫
    static BookTextService textService = (BookTextServiceImpl) BeanFactory
            .getBean("BookTextServiceImpl");

    // 阅读进度持久化
    static ReadingProgressDao instance = ReadingProgressDao.getInstance();

    // 阅读窗口滚动位置持久化
    static ReadSubscriptDao readSubscriptDao = ReadSubscriptDao.getInstance();

    // 页面设置持久化
    static WindowsSettingDao settingDao = WindowsSettingDao.getInstance();

    // 初始化数据
    private void init() {
        // 初始化表格
        searchBookTable.setModel(ModuleConstants.tableModel);
        searchBookTable.setEnabled(true);

        // 加载数据源下拉框
        for (String dataSourceName : ModuleConstants.DATA_SOURCE) {
            sourceDropdown.addItem(dataSourceName);
        }

        // btnSearch.setMargin(new Insets(0, 0, 0, 0));//设置边距
        // btnSearch.setBorderPainted(false);//不绘制边框
        // btnSearch.setFocusPainted(false);//选中后不绘制边框
        // btnSearch.setContentAreaFilled(false);//不绘制按钮区域
        // btnSearch.setText("");//不显示文字


        // 设置表格内容大小
        tablePane.setPreferredSize(new Dimension(-1, 30));

        // 加载组件配置信息
        ModuleUtils.loadModuleConfig(paneTextContent, scrollSpacing);

        // 加载提示信息
        ModuleUtils.loadComponentTooltip(btnSearch, openBook, btnOn, underOn, jumpButton,
                fontSizeDown, fontSizeUp, scrollSpacing);

        // 加载阅读进度
        ReadingUtils.loadReadingProgress(chapterList, textContent);

        // 加载持久化的设置
        ModuleUtils.loadSetting(paneTextContent, textContent, scrollSpacing);
    }

    // 页面初始化加载
    public MainUI(Project project, ToolWindow toolWindow) {
        this.project = project;
        // 执行初始化表格
        init();

        // 搜索
        btnSearch.addActionListener(e -> {

            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);
            // 清空表格数据
            ModuleConstants.tableModel.setRowCount(0);
            // 获取搜索输入文本
            bookSearchName = textSearchBar.getText();

            if (bookSearchName == null || bookSearchName.equals("")) {
                ToastUtils.showToastMassage(project, "请输入书籍名称", ToastType.ERROR);
                // 等待鼠标样式
                ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 获取数据源类型
            instance.searchType = Objects.requireNonNull(sourceDropdown.getSelectedItem()).toString();

            // 重置 重试次数
            BookSearchServiceImpl.index = 2;

            // 根据数据源类型 搜索
            new SearchBooks().execute();
        });

        // 开始阅读
        openBook.addActionListener(e -> {

            // 等待鼠标样式
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);

            // 获取选中行数据
            int selectedRow = searchBookTable.getSelectedRow();

            if (selectedRow < 0) {
                ToastUtils.showToastMassage(project, "还没有选择要读哪本书", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 获取书籍链接
            valueAt = searchBookTable.getValueAt(selectedRow, 4).toString();

            // 重置重试次数
            BookChapterServiceImpl.index = 2;

            // 执行开始阅读
            new StartReading().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 上一章节跳转
        btnOn.addActionListener(e -> {
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);

            if (instance.chapters.size() == 0 || instance.nowChapterIndex == 0) {
                ToastUtils.showToastMassage(project, "已经是第一章了", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
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
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);

            if (instance.chapters.size() == 0 || instance.nowChapterIndex == instance.chapters.size()) {
                ToastUtils.showToastMassage(project, "已经是最后一章了", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
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
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);

            // 根据下标跳转
            instance.nowChapterIndex = chapterList.getSelectedIndex();

            if (instance.chapters.size() == 0 || instance.nowChapterIndex < 0) {
                ToastUtils.showToastMassage(project, "未知章节", ToastType.ERROR);
                // 恢复默认鼠标样式
                ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
                return;
            }

            // 加载阅读信息
            new LoadChapterInformation().execute();

            // 阅读进度持久化
            instance.loadState(instance);
        });

        // 字号调小
        fontSizeDown.addActionListener(e -> {
            if (settingDao.fontSize == 1) {
                ToastUtils.showToastMassage(project, "已经是最小的了", ToastType.ERROR);
                return;
            }
            // 调小字体
            settingDao.fontSize--;
            textContent.setFont(new Font("", Font.BOLD, settingDao.fontSize));
            // 持久化
            settingDao.loadState(settingDao);
        });

        // 字体调大
        fontSizeUp.addActionListener(e -> {
            // 调大字体
            settingDao.fontSize++;
            textContent.setFont(new Font("", Font.BOLD, settingDao.fontSize));
            // 持久化
            settingDao.loadState(settingDao);
        });

        // 滑块滑动
        scrollSpacing.addChangeListener(e -> {
            JSlider jSlider = (JSlider) e.getSource();
            // 判断滑块是否停止
            if (!jSlider.getValueIsAdjusting()) {
                paneTextContent.getVerticalScrollBar().setUnitIncrement(jSlider.getValue());
            }
            // 持久化
            settingDao.scrollSpacingScale = jSlider.getValue();
            settingDao.loadState(settingDao);
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
                    ModuleUtils.loadSetting(paneTextContent, textContent, scrollSpacing);

                    ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.WAIT_CURSOR);

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
                    ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
                }
            });
            toolWindow.installWatcher(contentManager);
        });

        // 设置单击事件
        settingBtn.addActionListener(e -> {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Com.Liao.Book.Configurable.SettingConfigurable");
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
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
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
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
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
            BookTextServiceImpl.index = 2;

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
            ModuleUtils.loadTheMouseStyle(bookMainJPanel, Cursor.DEFAULT_CURSOR);
        }
    }

    // 窗口信息
    public JPanel getBookMainJPanel() {
        return bookMainJPanel;
    }

    private void createUIComponents() {
    }
}

