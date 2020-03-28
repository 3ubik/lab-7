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
	
	private static final String FRAME_TITLE = "Êëèåíò ìãíîâåííûõ ñîîáùåíèé";
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

		// Öåíòðèðîâàíèå îêíà
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth()) / 2, (kit.getScreenSize().height - getHeight()) / 2);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu chatMenu = new JMenu("Ìåíþ");
		menuBar.add(chatMenu);
		
		Action logInAction = new AbstractAction("Âõîä") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Ââåäèòå èìÿ äëÿ îáùåíèÿ", "Âõîä", JOptionPane.QUESTION_MESSAGE);
				messenger.setSender(value);
			}
		};
		
		Action findUserAction = new AbstractAction("Ïîèñê ïîëüçîâàòåëÿ") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Ââåäèòå èìÿ äëÿ ïîèñêà", "Ïîèñê ïîëüçîâàòåëÿ", JOptionPane.QUESTION_MESSAGE);
				
				User user;
				if (messenger.getDataBase().getUser(value) == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Ïîëüçîâàòåëÿ "+ value + " íå ñóùåñòâóåò", "Îøèáêà", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					user = messenger.getDataBase().getUser(value);
					JOptionPane.showMessageDialog(MainFrame.this, "Ïîëüçîâàòåëü íàéäåí!\n "+ user.getName() + " íàõîäèòñÿ â íàøåé áàçå äàííûõ.", "Ïîëüçîâàòåëü "+ user.getName(), JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		};
		
		Action openPrivateDialogAction = new AbstractAction("Ëè÷íîå ñîîáùåíèå") {
			public void actionPerformed(ActionEvent arg0) {
				String value = JOptionPane.showInputDialog(MainFrame.this, "Êîìó: ", "Ïîèñê ïîëüçîâàòåëÿ", JOptionPane.QUESTION_MESSAGE);
				
				User user;
				if (messenger.getDataBase().getUser(value) == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Ïîëüçîâàòåëÿ "+ value + " íå ñóùåñòâóåò", "Îøèáêà", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					user = messenger.getDataBase().getUser(value);
				}
				PrivateDialogFrame dialogFrame = new PrivateDialogFrame(user, MainFrame.this);
			}
		};
		
		chatMenu.add(logInAction);
		chatMenu.add(findUserAction);
		chatMenu.add(openPrivateDialogAction);
		
		// Òåêñòîâàÿ îáëàñòü äëÿ îòîáðàæåíèÿ ïîëó÷åííûõ ñîîáùåíèé
		textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
		textAreaIncoming.setEditable(false);
		
		// Êîíòåéíåð, îáåñïå÷èâàþùèé ïðîêðóòêó òåêñòîâîé îáëàñòè
		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming);
		
		// Ïîäïèñè ïîëåé
		final JLabel labelFrom = new JLabel("Îò");
		final JLabel labelTo = new JLabel("Ïîëó÷àòåëü");
		
		// Ïîëÿ ââîäà èìåíè ïîëüçîâàòåëÿ è àäðåñà ïîëó÷àòåëÿ
		textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
		textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
		
		// Òåêñòîâàÿ îáëàñòü äëÿ ââîäà ñîîáùåíèÿ
		textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
		
		// Êîíòåéíåð, îáåñïå÷èâàþùèé ïðîêðóòêó òåêñòîâîé îáëàñòè
		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing);
		
		// Ïàíåëü ââîäà ñîîáùåíèÿ
		final JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder("Ñîîáùåíèå"));

		// Êíîïêà îòïðàâêè ñîîáùåíèÿ
		final JButton sendButton = new JButton("Îòïðàâèòü");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messenger.sendMessage(textFieldTo.getText(),
						textAreaOutgoing.getText());
			}
		});

		// Êîìïîíîâêà ýëåìåíòîâ ïàíåëè "Ñîîáùåíèå"
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

		// Êîìïîíîâêà ýëåìåíòîâ ôðåéìà
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