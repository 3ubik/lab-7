package lab7;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
	private static final int FRAME_MINIMUM_WIDTH = 500;
	private static final int FRAME_MINIMUM_HEIGHT = 500;
	private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;
	private static final int TO_FIELD_DEFAULT_COLUMNS = 20;
	private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
	private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
	private static final int SMALL_GAP = 5;
	private static final int MEDIUM_GAP = 10;
	private static final int LARGE_GAP = 15;
	private static final int SERVER_PORT = 4567;
	private final JTextField textFieldFrom;
	private final JTextField textFieldTo;
	private final JTextArea textAreaIncoming;
	private final JTextArea textAreaOutgoing;
	
	private InstantMessenger messenger;
	public InstantMessenger getMessenger() {
		return messenger;
	}
	
	public MainFrame() {
		super(FRAME_TITLE);
		setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));

		
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu chatMenu = new JMenu("Меню");
		menuBar.add(chatMenu);
		
		Action logInAction = new AbstractAction("Вход") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Введите имя для общения", "Вход", JOptionPane.QUESTION_MESSAGE);
				messenger.setSender(value);
			}
		};
		
		Action findUserAction = new AbstractAction("Поиск пользователя") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Введите имя для поиска", "Поиск пользователя", JOptionPane.QUESTION_MESSAGE);
				
				User user;
				if (messenger.getDataBase().getUser(value) == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Пользователя "+ value + " не существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					user = messenger.getDataBase().getUser(value);
					JOptionPane.showMessageDialog(MainFrame.this, "Пользователь найден!\n "+ user.getName() + "находится в базе данных.", "Пользователь "+ user.getName(), JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		};
		
		Action openPrivateDialogAction = new AbstractAction("Личное сообщение") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Кому: ", "Поиск пользователя", JOptionPane.QUESTION_MESSAGE);
				
				User user;
				if (messenger.getDataBase().getUser(value) == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Пользователя "+ value + " Не существует", "Ошибка", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					user = messenger.getDataBase().getUser(value);
				}
				DialogFrame dialogFrame = new DialogFrame(user, MainFrame.this);
			}
		};
		
		chatMenu.add(logInAction);
		chatMenu.add(findUserAction);
		chatMenu.add(openPrivateDialogAction);
		
		// Текстовая область для отображения полученных сообщений
		textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
		textAreaIncoming.setEditable(false);
		
		// Контейнер, обеспечивающий прокрутку текстовой области
		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
		
		// Подписи полей
		final JLabel labelFrom = new JLabel("От");
		final JLabel labelTo = new JLabel("Получатель");
		
		// Поля ввода имени пользователя и адреса получателя
		textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
		textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
		
		// Текстовая область для ввода сообщения
		textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
		
		// Контейнер, обеспечивающий прокрутку текстовой области
		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
		
		// Панель ввода сообщения
		final JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение"));

		// Кнопка отправки сообщения
		final JButton sendButton = new JButton("Отправить");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messenger.sendMessage(textFieldTo.getText(),
						textAreaOutgoing.getText());
			}
		});

		// Компоновка элементов панели "Сообщение"
		final GroupLayout layout2 = new GroupLayout(messagePanel);
		messagePanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout2.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout2.createSequentialGroup()
								.addGap(LARGE_GAP)
								.addComponent(labelTo)
								.addGap(SMALL_GAP)
								.addComponent(textFieldTo))
							.addComponent(scrollPaneOutgoing)
							.addComponent(sendButton))
				.addContainerGap());
			layout2.setVerticalGroup(layout2.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout2.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTo)
						.addComponent(textFieldTo))
					.addGap(MEDIUM_GAP)
					.addComponent(scrollPaneOutgoing)
					.addGap(MEDIUM_GAP)
					.addComponent(sendButton)
				.addContainerGap());

			// Компоновка элементов фрейма
		final GroupLayout layout1 = new GroupLayout(getContentPane());
		setLayout(layout1);
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout1.createParallelGroup()
						.addComponent(scrollPaneIncoming)
						.addComponent(messagePanel))
				.addContainerGap());
		layout1.setVerticalGroup(layout1.createSequentialGroup()
			.addContainerGap()
			.addComponent(scrollPaneIncoming)
			.addGap(MEDIUM_GAP)
			.addComponent(messagePanel)
			.addContainerGap());
		messenger = new InstantMessenger(this);
	}

	public JTextArea getTextAreaOutgoing() {
		return textAreaOutgoing;
	}

	public int getServerPort() {
		return SERVER_PORT;
	}

	public JTextArea getTextAreaIncoming() {
		return textAreaIncoming;
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}