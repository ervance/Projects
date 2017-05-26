public class Test1 {

	public static void main(String[] args) {
		String ans[] = {
				"",
				"man-0",
				"(man-1)tax-0",
				"cat-0(man-1(tax-2))",
				"cat is there.",
				"cat-0(man-1(tax-2))",
				"man is there.",
				"(cat-1)man-0(tax-1)",
				"tax is there.",
				"((cat-2)man-1)tax-0",
				"cat is there.",
				"cat-0(man-1(tax-2))",
				"tea is not.",
				"((cat-2)man-1)tax-0",
				"dog is not.",
				"cat-0(man-1(tax-2))",
				"dog is not.",
				"(cat-1)man-0(tax-1)",
				"dog is not.",
				"cat-0(man-1(tax-2))",
				"ant is not.",
				"cat-0(man-1(tax-2))",
				"cat-0(man-1(tax-2))",
				"((cat-2)man-1)tax-0",
				"(cat-1)man-0(tax-1)",
				"(((ant-3)cup-2)goo-1)jam-0",
				"(ant-1)bug-0((cup-2)goo-1(jam-2))",
				"((ant-2)bug-1)cat-0(cup-1(goo-2(jam-3)))",
				"(((ant-3)bug-2)cat-1(cup-2))elf-0(goo-1(jam-2))",
				"((((ant-4)bug-3)cat-2(cup-3))elf-1)fog-0(goo-1(jam-2))",
				"((((ant-4)bug-3)cat-2)cup-1)dog-0(elf-1(fog-2(goo-3(jam-4))))",
				"(ant-1)boy-0(((bug-3)cat-2(cup-3))dog-1(elf-2(fog-3(goo-4(jam-5)))))",
				"(((ant-3)boy-2((bug-4)cat-3(cup-4)))dog-1((elf-3)fog-2(goo-3)))gum-0(jam-1)",
				"cup is there.",
				"(((ant-3)boy-2(bug-3))cat-1)cup-0(dog-1(((elf-4)fog-3(goo-4))gum-2(jam-3)))",
				"dog is there.",
				"((((ant-4)boy-3(bug-4))cat-2)cup-1)dog-0(((elf-3)fog-2(goo-3))gum-1(jam-2))",
				"bug is there.",
				"((ant-2)boy-1)bug-0((cat-2)cup-1(dog-2(((elf-5)fog-4(goo-5))gum-3(jam-4))))",
				"((((ant-4)boy-3)bug-2(cat-3))cup-1(dog-2(elf-3)))fog-0((goo-2)gum-1(jam-2))",
				"cat is there.",
				"(((ant-3)boy-2)bug-1)cat-0((cup-2(dog-3(elf-4)))fog-1((goo-3)gum-2(jam-3)))",
				"(ant-1)boy-0(bug-1(cat-2((cup-4(dog-5(elf-6)))fog-3((goo-5)gum-4(jam-5)))))",
				"dog is there.",
				"((ant-2)boy-1((bug-3)cat-2(cup-3)))dog-0((elf-2)fog-1((goo-3)gum-2(jam-3)))",
				"gat is not.",
				"(((ant-3)boy-2((bug-4)cat-3(cup-4)))dog-1((elf-3)fog-2))goo-0(gum-1(jam-2))",
				"gat is not.",
				"(((ant-3)boy-2((bug-4)cat-3(cup-4)))dog-1(elf-2))fog-0(goo-1(gum-2(jam-3)))",
				"act is not.",
				"ant-0((boy-2(((bug-5)cat-4(cup-5))dog-3(elf-4)))fog-1(goo-2(gum-3(jam-4))))",
				"jam is there.",
				"((ant-2(boy-3(((bug-6)cat-5(cup-6))dog-4(elf-5))))fog-1((goo-3)gum-2))jam-0",
				"cot is not.",
				"((ant-2)boy-1((bug-3)cat-2))cup-0((dog-2(elf-3))fog-1(((goo-4)gum-3)jam-2))",
				"((ant-2)boy-1((bug-3)cat-2))cup-0((dog-2(elf-3))fog-1(((goo-4)gum-3)jam-2))",
				"((ant-2)boy-1(((bug-4)cat-3)cup-2))dog-0((elf-2(fog-3))goo-1((gum-3)jam-2))",
				"(10-1)12-0((((14-4((((17-8(23-9))25-7(28-8))30-6(31-7))33-5((35-7)36-6)))37-3(((39-6(42-7))43-5((48-7)49-6))53-4(((56-7(58-8))59-6(62-7))65-5)))67-2(70-3(72-4((73-6)77-5((79-7)80-6)))))81-1(((82-4(((83-7)86-6)88-5))90-3(92-4))96-2))",
				"(16-1(19-2(31-3(38-4))))51-0(((54-3)59-2)66-1(76-2(93-3)))",
				"(((10-3)11-2)12-1(((13-4)17-3)18-2))19-0((20-2(((22-5(24-6((25-8)26-7)))27-4((28-6)29-5))30-3(((((((32-10)33-9)34-8(36-9))37-7)38-6(39-7(41-8)))42-5)43-4)))44-1((((48-5(49-6))50-4((51-6((53-8(54-9))55-7))56-5))57-3((58-5(59-6))62-4((63-6(67-7))68-5)))70-2((((72-6)73-5((74-7(75-8))76-6(77-7)))78-4((79-6)80-5))81-3(((((82-8((83-10)85-9))88-7)89-6((90-8)93-7))96-5)97-4))))",
				"((((10029-4)10164-3)10672-2(10751-3(10808-4)))1084-1)10910-0(((((11071-5(((11549-8)11619-7)11794-6))12909-4)14626-3(((15260-6(((15381-9)1541-8)16227-7))16983-5)17168-4(((17898-7(17998-8))18642-6)1895-5((19253-7(19546-8))20215-6))))20234-2(((21208-5((((21236-9(21771-10))22394-8(22450-9))22805-7)22944-6(22984-7(((24545-10(((26394-13(27350-14(28192-15(28211-16))))2829-12(28898-13))29285-11))29808-9((29820-11)30271-10(30602-11)))31749-8(32242-9(3372-10(33750-11)))))))34008-4((34513-6)34551-5))34629-3(((36426-6(36714-7(37265-8)))37644-5((((((3789-11(38056-12))38470-10)38776-9)38814-8(3959-9((39718-11((40555-13)40558-12))40955-10)))41330-7(41583-8))41752-6(41800-7)))42045-4)))43551-1((((43657-5(44653-6(44660-7(44765-8))))46546-4((47216-6(47398-7))48582-5(49165-6)))50696-3(((51168-6(52013-7))53371-5)53581-4))54565-2((((54679-6(((55135-9)55504-8)55591-7((((56464-11(((56536-14(5675-15))5719-13)57285-12((57789-14)58390-13(((59601-16)60470-15)61468-14))))61771-10)62237-9(62255-10(((62417-13)62676-12(62975-13))63610-11((63646-13(64738-14))64903-12))))64906-8((65272-10((65573-12)65618-11))65719-9((((65758-13)65827-12(((65991-15)66446-14)66489-13))6653-11)6660-10(66968-11(67383-12(67427-13((67916-15)67936-14)))))))))6825-5((((((((((68694-15)68723-14)71804-13)71903-12)71937-11((72194-13(72580-14((72937-16((73248-18)73346-17))7375-15((74003-17)74172-16))))74241-12(7555-13(75716-14))))77296-10((((77451-14)77569-13)77942-12(((78869-15)78967-14(8028-15))8208-13))82118-11(((82579-14((8263-16)82819-15))8500-13(85564-14))85690-12((85801-14)85802-13(85817-14)))))85962-9(((8631-12)86851-11(87106-12))87750-10))89338-8((89399-10(89502-11(90469-12)))90746-9(91174-10)))91318-7((((((91403-13)91424-12((91648-14)92374-13))92753-11)93085-10)93713-9)93997-8(((9418-11)9527-10((95936-12)96359-11))97325-9)))97533-6))97692-4((9771-6(97950-7))98210-5))98562-3(98788-4(99734-5(99932-6))))))"
		};
		SplayStrings t = new SplayStrings(), t1 = new SplayStrings();
		char action;
		int i, dot, ct, num, ran = 87, ansCt = 0;
		String str, check;

		// Phase 1 3-element tree inserts and searches
		String s = "o.iman.o.itax.o.icat.o.scat.o.sman.o.stax.o.scat.o.stea.o.sd";
		s = s + "og.o.sdog.o.sdog.o.sant.o.icat.o.itax.o.iman.o.p3-element tree ";
		s = s + "checks.";

		// Phase 2 11-element tree inserts and searches
		s = s + "n.iant.icup.igoo.ijam.o.ibug.o.icat.o.ielf.o.ifog.o.idog.o.iboy";
		s = s + ".o.igum.o.scup.o.sdog.o.sbug.o.ifog.o.scat.o.iboy.o.sdog.o.sgat";
		s = s + ".o.sgat.o.sact.o.sjam.o.scot.o.icup.o.Sbat.Sget.Send.Sget.Sbat.";
		s = s + "Sdig.Sfig.Sjig.Sgig.Sbig.Sdig.o.p11-element inserts and searche";
		s = s + "s.";

		// Phase 3 a bunch of random inserts, searches, and deletes
		s = s + "n.r50.o.n.r10.o.n.r100.o.n.R200.o.prandom";
		s = s + " tests.";

		System.out.println(SplayStrings.myName());
		do {
			action = s.charAt(0);
			dot = s.indexOf('.');
			str = s.substring(1, dot);
			s = s.substring(dot + 1, s.length());
			if (action == 'i')
				t.insert(str);
			else if (action == 'o') {
				if (t.toString().compareTo(ans[ansCt]) == 0)
					System.out.print("OK. \n" + t + "\n");
				else
					System.out.println("***  error   ***\n" + t + "\n" + ans[ansCt] + " action : insert fail - " + ansCt);
				ansCt++;
				if (ansCt % 8 == 0) System.out.println();
			}
			else if (action == 's') {
				if (t.search(str) == null)
					check = str + " is not.";
				else
					check = str + " is there.";
				if (check.compareTo(ans[ansCt]) == 0)
					System.out.print("OK. \n" + "Search Result : " + t + "\n Was searching for: " + str + "\n ***END*** \n");
				else
					System.out.println("***  error   ***\n" + t + "\n" + ans[ansCt] + " action : search fail - " + ansCt + "Search Result : " + t + "\n Was searching for: " + str + "\n ***END*** \n");
				ansCt++;
				if (ansCt % 8 == 0) System.out.println();
			}
			else if (action == 'S')
				t.search(str);
			else if (action == 'n')
				t = new SplayStrings();
			else if (action == 'p') {
				System.out.println("\n****   End of " + str + ".   ****");
				System.out.println();
			} else if (action == 'r') {
				num = 0;
				for (i = 0; i < str.length(); i++)
					num = 10 * num + str.charAt(i) - '0';
				for (ct = 1; ct <= num; ct++) {
					ran = (ran * 101 + 103) % 1000003;
					t.insert(String.valueOf(ran % 90 + 10));

				}
				num = ran % 100;
			} else if (action == 'R') {
				num = 0;
				for (i = 0; i < str.length(); i++)
					num = 10 * num + str.charAt(i) - '0';
				for (ct = 1; ct <= num; ct++) {
					ran = (ran * 101 + 103) % 1000003;
					t.insert(String.valueOf(ran % 100000));

				}
				num = ran % 100;
			} else
				System.out.print("Illegal operation " + action
						+ " in input string.");
		} while (s.length() != 0);
		System.out.println();
		System.out.println("All phases complete.");
	}
}