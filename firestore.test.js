const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validProfilePayload(uid, now = new Date(Date.now() - 2000)) {
  return {
    userId: uid,
    displayName: "Abdullah",
    niyyahTitle: "Clean Heart & Peace with Allah",
    challengeStartMillis: Date.now() - 3600000,
    targetDays: 40,
    longestStreakDays: 0,
    urgesOvercomeCount: 0,
    totalResetsCount: 0,
    completedEpisodes: [],
    createdAt: now,
    updatedAt: now,
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: denied all reads and writes", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set(validProfilePayload(ALICE_UID))
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("urgeLogs").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("episodeNotes").get()
  );
});

test("UserChallengeProfile: owner create, partial updates, and validation bounds", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const profileRef = aliceDb.collection("users").doc(ALICE_UID);

  // Valid creation
  await assertSucceeds(profileRef.set(validProfilePayload(ALICE_UID)));

  // Action 1: Update challenge start date/time and niyyahTitle
  await assertSucceeds(
    profileRef.update({
      niyyahTitle: "40 Days of Sabr and Istighfar",
      challengeStartMillis: Date.now() - 86400000 * 3,
      targetDays: 90,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Action 2: Record urge outcome
  await assertSucceeds(
    profileRef.update({
      urgesOvercomeCount: 1,
      longestStreakDays: 3,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Action 3: Update completedEpisodes
  await assertSucceeds(
    profileRef.update({
      completedEpisodes: [1, 2],
      updatedAt: new Date(Date.now() - 200),
    })
  );

  // Reject ghost field (shadow update)
  await assertFails(
    profileRef.update({
      completedEpisodes: [1, 2, 3],
      isAdmin: true,
      updatedAt: new Date(Date.now() - 100),
    })
  );

  // Reject immutable createdAt modification
  await assertFails(
    profileRef.update({
      createdAt: new Date(Date.now() - 100),
      updatedAt: new Date(Date.now() - 100),
    })
  );

  // Reject future timestamp
  await assertFails(
    profileRef.update({
      targetDays: 30,
      updatedAt: new Date(Date.now() + 60000),
    })
  );

  // Reject invalid @DocumentId 'id' field on create
  await assertFails(
    aliceDb
      .collection("users")
      .doc("alice_other")
      .set({
        ...validProfilePayload(ALICE_UID),
        id: "alice_other",
      })
  );
});

test("Cross-user isolation: Alice cannot read or modify Bob's data", async () => {
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    const db = ctx.firestore();
    await db.collection("users").doc(BOB_UID).set(validProfilePayload(BOB_UID));
    await db
      .collection("users")
      .doc(BOB_UID)
      .collection("urgeLogs")
      .doc("log_1")
      .set({
        userId: BOB_UID,
        outcome: "OVERCOME",
        triggerNote: "Late night boredom",
        intensity: 4,
        dhikrCount: 33,
        createdAt: new Date(Date.now() - 2000),
      });
    await db
      .collection("users")
      .doc(BOB_UID)
      .collection("episodeNotes")
      .doc("ep_1")
      .set({
        userId: BOB_UID,
        episodeNumber: 1,
        reflection: "Starting with sincerity.",
        completed: true,
        createdAt: new Date(Date.now() - 2000),
        updatedAt: new Date(Date.now() - 2000),
      });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).update({ targetDays: 10 })
  );
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("urgeLogs").get()
  );
  await assertFails(
    aliceDb.collection("users").doc(BOB_UID).collection("episodeNotes").get()
  );
});

test("UrgeLogs subcollection: create, list, immutable update denial, and validation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const logsCol = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("urgeLogs");

  // List on empty subcollection must succeed
  await assertSucceeds(logsCol.get());

  // Valid create
  await assertSucceeds(
    logsCol.doc("log_1").set({
      userId: ALICE_UID,
      outcome: "OVERCOME",
      triggerNote: "Felt stressed after work, did wudu and dhikr.",
      intensity: 4,
      dhikrCount: 33,
      createdAt: new Date(Date.now() - 1000),
    })
  );

  // List after create succeeds
  await assertSucceeds(logsCol.get());

  // Update is forbidden on immutable log
  await assertFails(logsCol.doc("log_1").update({ intensity: 1 }));

  // Invalid outcome enum rejected
  await assertFails(
    logsCol.doc("log_2").set({
      userId: ALICE_UID,
      outcome: "UNKNOWN",
      triggerNote: "",
      intensity: 3,
      dhikrCount: 0,
      createdAt: new Date(Date.now() - 1000),
    })
  );

  // Invalid intensity out of bounds rejected
  await assertFails(
    logsCol.doc("log_3").set({
      userId: ALICE_UID,
      outcome: "SLIP",
      triggerNote: "",
      intensity: 6,
      dhikrCount: 0,
      createdAt: new Date(Date.now() - 1000),
    })
  );
});

test("EpisodeNotes subcollection: create ep_1..ep_11, update, and reject invalid IDs", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const notesCol = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("episodeNotes");

  await assertSucceeds(notesCol.get());

  const now = new Date(Date.now() - 5000);
  await assertSucceeds(
    notesCol.doc("ep_1").set({
      userId: ALICE_UID,
      episodeNumber: 1,
      reflection: "Allah sees every hidden battle.",
      completed: true,
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    notesCol.doc("ep_11").set({
      userId: ALICE_UID,
      episodeNumber: 11,
      reflection: "Gratitude and lifelong vigilance.",
      completed: false,
      createdAt: now,
      updatedAt: now,
    })
  );

  // Valid partial update
  await assertSucceeds(
    notesCol.doc("ep_11").update({
      reflection: "Completed with gratitude to Allah.",
      completed: true,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reject out-of-range episode document ID
  await assertFails(
    notesCol.doc("ep_12").set({
      userId: ALICE_UID,
      episodeNumber: 11,
      reflection: "Invalid episode ID",
      completed: true,
      createdAt: now,
      updatedAt: now,
    })
  );
});
