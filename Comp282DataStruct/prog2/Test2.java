public class Test2 {

   public static void main(String[] args)
   {
		BSTStringsPlus t = new BSTStringsPlus();
		String str;
		int ct, ran = 87, line = 1, ansct = 0, num;
		boolean delete = false, rotate = false, testFailure = false;
		char action;
		String searchTest = "";
		String s = "oimaoinaoioaoipaoiqaoilaoikaoikdikgikfikeoinaikgikaiqaioaoI20oI99onI30o";
		s += "sgxcscstsmldsaaaszzzsczzsbxaslovsieisnfnsicjS";
		s += "lgxcormldorcerolbpjorvkgryzmllovliodlgggrmmmlieioriodo";
		s += "dhcrodicjodieiodkhxodiododzzzodnzzo";
		s += "dourodnrmodmldodlovodgxcodfclodsjvodpmtodaaadzzzdmmmojnggjngzdpihojtttdxehdwcsdvkgonI99D5000oD9999oD9999D9999o";
		String ans[] = {
				"0 0 0 0  none",
				"ma-01 1 1 0  none",
				"ma-0(na-1)1 2 2 1  na",
				"ma-0(na-1(oa-2))1 3 3 2  na",
				"ma-0(na-1(oa-2(pa-3)))1 4 4 3  na",
				"ma-0(na-1(oa-2(pa-3(qa-4))))1 5 5 4  na",
				"(la-1)ma-0(na-1(oa-2(pa-3(qa-4))))2 5 2 3  ma",
				"((ka-2)la-1)ma-0(na-1(oa-2(pa-3(qa-4))))2 5 3 4  la",
				"((ka-2(kd-3(((ke-6)kf-5)kg-4)))la-1)ma-0(na-1(oa-2(pa-3(qa-4))))2 7 5 8  kd",
				"((ka-2(kd-3(((ke-6)kf-5)kg-4)))la-1)ma-0(na-1(oa-2(pa-3(qa-4))))2 7 5 8  kd",
				"(((aqu-3(cdf-4(ejc-5(hdo-6))))ka-2((kae-4)kd-3(((ke-6)kf-5)kg-4)))la-1(lhx-2(lzc-3)))ma-0((mlh-2(mrg-3))na-1((naj-3)oa-2((orq-4)pa-3((pln-5(pqr-6))qa-4(((qiq-7((sgb-9)tvb-8(uem-9)))uwp-6)yfo-5(zif-6))))))11 10 4 10  cdf",
				"(((((aka-5(apa-6))aqs-4)aqu-3((axb-5)cdf-4(((((((cff-11)chj-10)clw-9(cug-10))cxs-8)ddg-7)dkr-6(dpx-7(dsx-8((dvd-10)dyp-9(edc-10)))))ejc-5(((((epa-10)esn-9(fgq-10))fwu-8(gbg-9(giz-10((gly-12)gma-11))))gws-7)hdo-6(((hkx-9)hqv-8(iry-9(ive-10)))ixt-7((ixy-9)jgz-8((joh-10)jyc-9)))))))ka-2((kae-4(kao-5))kd-3(((ke-6)kf-5)kg-4((kjj-6)kpc-5))))la-1(((lds-4)lev-3)lhx-2(((lne-5)loj-4(lsc-5))lzc-3)))ma-0(((mfc-3)mlh-2(mrg-3(mrj-4(mwq-5))))na-1((naj-3((net-5(nmn-6))nnq-4((nps-6)nvq-5)))oa-2((((ocx-6)ogx-5(onb-6))orq-4(otq-5))pa-3((pln-5((pph-7)pqr-6(pzv-7)))qa-4((((qei-8(qez-9))qiq-7(((((qpx-12(qrs-13))qsm-11((qyc-13)rfn-12))rku-10(rmq-11((rpr-13(rxu-14))seh-12)))sgb-9(shl-10((shv-12(ssz-13))tdl-11(tje-12(tqf-13)))))tvb-8((ucx-10(uel-11))uem-9(uio-10(uje-11)))))uwp-6((((vco-10((vik-12)vlx-11))vod-9(((vzc-12)wdk-11(wkp-12))wmh-10(wua-11)))xic-8(xjt-9(((xmp-12)xvh-11(xvz-12))yan-10)))ybh-7))yfo-5((((yil-9)ynt-8(ysk-9))zfc-7)zif-6(zqc-7)))))))45 15 4 41  apa",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 7  bpj",
				"gxccstmldnullnullnullbxalovieinfnicj",
				"((blp-2(((bpj-5(bxa-6))cer-4(cfw-5))cst-3(fcl-4)))gxc-1((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3)))mld-0(((nfn-3)njv-2(((nrm-5)our-4((pih-6)pmt-5))sjv-3((sjw-5(vkg-6))wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))12 7 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 7  bpj",
				"(blp-1((bpj-3((bxa-5)cer-4(cfw-5)))cst-2(fcl-3)))gxc-0(((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((((((hcr-6)icj-5)iei-4)iod-3)khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))11 8 4 9  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((((hcr-5)icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((((icj-4)iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))12 8 4 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((iei-3(iod-4))khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))11 8 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((iod-3)khx-2(lov-3))mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))11 8 4 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(((iod-3)lov-2)mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))10 8 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))10 8 3 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))10 8 3 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)mld-1(((nfn-4)njv-3(((nrm-6)our-5((pih-7)pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))10 8 3 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)mld-1(((nfn-4)njv-3(((nrm-6)pih-5(pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))10 8 3 5  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)mld-1(((nfn-4)njv-3((pih-5(pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))9 8 3 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0((lov-2)nfn-1((njv-3((pih-5(pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))8 8 3 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))gxc-0(nfn-1((njv-3((pih-5(pmt-6))sjv-4((sjw-6(vkg-7))wcs-5)))xeh-2((yif-4(yzm-5))znv-3(zue-4))))7 8 4 8  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2(fcl-3)))nfn-0((njv-2((pih-4(pmt-5))sjv-3((sjw-5(vkg-6))wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))7 7 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0((njv-2((pih-4(pmt-5))sjv-3((sjw-5(vkg-6))wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))6 7 4 8  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0((njv-2((pih-4(pmt-5))sjw-3((vkg-5)wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))6 6 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0((njv-2((pih-4)sjw-3((vkg-5)wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))6 6 4 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0((njv-2((pih-4)sjw-3((vkg-5)wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))6 6 4 6  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0(((ngg-3(ngz-4))njv-2(sjw-3((vkg-5)wcs-4)))xeh-1((yif-3(yzm-4))znv-2(zue-3)))6 6 4 7  bpj",
				"(blp-1(((bpj-4(bxa-5))cer-3(cfw-4))cst-2))nfn-0(((ngg-3(ngz-4))njv-2(sjw-3(ttt-4)))yif-1((yzm-3)znv-2(zue-3)))6 6 4 5  bpj",
				"(((all-3((art-5((azx-7)bcf-6(bgf-7)))bmx-4(((cmv-7((cxk-9)dec-8(dnx-9)))drg-6(dub-7((((duv-11)dxd-10)edn-9((egv-11)equ-10))eqv-8)))etj-5(fnu-6))))ftz-2(((((gcq-7)gjo-6)gkn-5(gnw-6((got-8)gve-7)))hfk-4((hgv-6)hpe-5((hwz-7)ijs-6(izb-7))))jtq-3(((((jul-8)kab-7(kaq-8(kqh-9)))kti-6)kyy-5(((ldu-8)lpm-7(ltc-8))mpv-6))mxx-4(nbe-5))))ncj-1(((ndv-4)ngg-3(nhl-4))npp-2((nto-4)nux-3)))otl-0(((ozu-3)pmc-2)pyk-1((((pyv-5)qgk-4)qsv-3(((rij-6)rjs-5(((rsk-8)sau-7)ugq-6))vef-4(vsa-5((vxo-7)wgm-6((whg-8)wsz-7)))))xja-2((xrs-4)yyi-3(zrg-4))))28 12 4 20  art",
				"((((bgf-4)bmx-3(((cmv-6(dec-7(dnx-8)))drg-5(dub-6((dxd-8)egv-7(equ-8))))etj-4))ftz-2((((gcq-6)gnw-5(got-6))hfk-4(hwz-5(ijs-6(izb-7))))jtq-3((((kaq-7)kti-6)kyy-5(((ldu-8)lpm-7)mpv-6))mxx-4(nbe-5))))ndv-1((ngg-3(nhl-4))nto-2(nux-3)))pmc-0((qsv-2(((rij-5)rjs-4((rsk-6)sau-5))vef-3((whg-5)wsz-4)))xja-1((xrs-3)yyi-2))16 9 4 14  bmx",
				"(((dub-3)gcq-2((gnw-4)kaq-3((kti-5)ldu-4((lpm-6)mpv-5))))ndv-1((ngg-3(nhl-4))nto-2))qsv-0(((rij-3)sau-2)whg-1)6 7 4 5  gcq"
				};

		do {
			action = s.charAt(0);
			// System.out.println();
			// System.out.println("action: " + action);
			if (action == 'i') { // insert
				str = s.substring(1, 3);
				s = s.substring(3, s.length());
				t.insert(str);
			} else if (action == 'j') { // insert
				str = s.substring(1, 4);
				s = s.substring(4, s.length());
				t.insert(str);
			} else if (action == 's') {
				str = s.substring(1, 4);
				s = s.substring(4, s.length());
				if (t.search(str) == null)
					searchTest += "null";
				else
					searchTest += t.search(str).getString();
			} else if (action == 'r') { //rotate right
				if (!rotate) {
					rotate = true;
					System.out.println();
					System.out.println("\nRotates begin.");
				}
				str = s.substring(1, 4);
				s = s.substring(4, s.length());
				t.rotateRight(str);
			} else if (action == 'l') { // rotate left
				if (!rotate) {
					rotate = true;
					System.out.println();
					System.out.println("\nRotates begin.");
				}
				str = s.substring(1, 4);
				s = s.substring(4, s.length());
				t.rotateLeft(str);
			} else if (action == 'd') { // delete
				if (!delete) {
					delete = true;
					System.out.println();
					System.out.println("Deletes begin.");
				}
				str = s.substring(1, 4);
				s = s.substring(4, s.length());
				t.delete(str);
			} else if (action == 'n') { // new tree -- wipe out the tree and
										// start over
				s = s.substring(1, s.length());
				t = new BSTStringsPlus();
			} else if (action == 'I') {
				num = (s.charAt(1) - '0') * 10 + s.charAt(2) - '0';
				s = s.substring(3, s.length());
				for (ct = 1; ct <= num; ct++) {
					ran = (ran * 101 + 103) % 1000003;
					str = String.valueOf((char) (ran % 26 + 'a'));
					ran = (ran * 101 + 103) % 1000003;
					str += String.valueOf((char) (ran % 26 + 'a'));
					ran = (ran * 101 + 103) % 1000003;
					str += String.valueOf((char) (ran % 26 + 'a'));
					t.insert(str);
				}
			} else if (action == 'D') {
				num = (s.charAt(1) - '0') * 1000 + (s.charAt(2) - '0') * 100
						+ (s.charAt(3) - '0') * 10 + s.charAt(4) - '0';
				s = s.substring(5, s.length());
				for (ct = 1; ct <= num; ct++) {
					ran = (ran * 101 + 103) % 1000003;
					str = String.valueOf((char) (ran % 26 + 'a'));
					ran = (ran * 101 + 103) % 1000003;
					str += String.valueOf((char) (ran % 26 + 'a'));
					ran = (ran * 101 + 103) % 1000003;
					str += String.valueOf((char) (ran % 26 + 'a'));
					t.delete(str);
				}
			} else if (action == 'o') { // check regular result
				s = s.substring(1, s.length());
				// System.out.println(t);
				System.out.print(line++ + ". ");
				if (t.toString2().compareTo(ans[ansct]) == 0) {
					System.out.print(" Answers match.   ");
					if (line % 4 == 1)
						System.out.println();
				} else if (t.toString2().substring(0, t.toString2().length() - 3).compareTo(ans[ansct].substring(0, ans[ansct].length() - 3)) == 0) {
						System.out.print(" ** 2nd small fail **   ");
						if (line % 4 == 1)
							System.out.println();
				} else {
					System.out.println("   *** NO MATCH ***   ");
					System.out.println(t.toString2());
				}
				ansct++;
			} else if (action == 'S') { // check search result
				System.out.println();
				System.out.println("\nChecking search.");
				s = s.substring(1, s.length());
				// System.out.println(searchTest);
				System.out.print(line++ + ". ");
				if (searchTest.compareTo(ans[ansct]) == 0) {
					System.out.print(" Answers match.   ");
					if (line % 4 == 1)
						System.out.println();
				} else {
					System.out.println("   *** NO MATCH ***   ");
					System.out.println(t.toString2());
				}
				ansct++;
			} else {
				System.out.println("No such action: " + action);
				testFailure = true;
			}
		} while (s.length() != 0 && !testFailure);
		System.out.print("\n\nChecking copy.  ");
		BSTStringsPlus t1 = new BSTStringsPlus(t);
		if (t.toString().compareTo(t1.toString()) == 0)
			System.out.print("OK.  ");
		else
			System.out.println("*** Copy failed ***");
		t.insert("change");
		if (t.toString().compareTo(t1.toString()) != 0)
			System.out.print("OK.  ");
		else
			System.out.println("*** Trees should be different ***");
		t1 = new BSTStringsPlus(t);
		if (t.toString().compareTo(t1.toString()) == 0)
			System.out.print("OK.  ");
		else
			System.out.println("*** Trees should be the same again ***");
		t1.insert("differ");
		if (t.toString().compareTo(t1.toString()) != 0)
			System.out.print("OK.  ");
		else
			System.out.println("*** Trees should be different ***");
		
		System.out.println("\n\nProgrammed by: " + BSTStrings.myName());
	}
}

class BSTStringsPlus extends BSTStrings {

	public BSTStringsPlus() {
		super();
	}

	public BSTStringsPlus(BSTStringsPlus t) {
		super(t);
	}

	public String toString2() {
		String s = new String(), small2;
		s = this.toString()
				+ String.valueOf(this.leafCt())
				+ " "
				+ String.valueOf(this.height())
				+ " "
				+ String.valueOf(this.closeness() + " "
						+ String.valueOf(this.jointChildCt()) + " ");
		if (this.secondSmallest() == null)
			small2 = "none";
		else
			small2 = String.valueOf(this.secondSmallest().getString());
		s += " " + small2;
		return s;
	}
}